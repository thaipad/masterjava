package ru.javaops.masterjava.upload;

import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.JaxbUnmarshaller;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class UserProcessor {
    private static final JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);
    private static final int NUMBER_THREADS = 4;
    private ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREADS);
    private UserDao dao = DBIProvider.getDao(UserDao.class);

    public static class FailedEmail {
        public String emailOrRange;
        public String reason;

        public FailedEmail(String emailOrRange, String reason) {
            this.emailOrRange = emailOrRange;
            this.reason = reason;
        }

        @Override
        public String toString() {
            return emailOrRange + " : " + reason;
        }
    }

    public List<FailedEmail> process(final InputStream is, int chunkSize) throws XMLStreamException, JAXBException {
        final StaxStreamProcessor processor = new StaxStreamProcessor(is);
        List<User> chunk = new ArrayList<>(chunkSize);

        Map<String, Future<List<String>>> futures = new LinkedHashMap<>();

        JaxbUnmarshaller unmarshaller = jaxbParser.createUnmarshaller();
        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
            ru.javaops.masterjava.xml.schema.User xmlUser = unmarshaller.unmarshal(processor.getReader(), ru.javaops.masterjava.xml.schema.User.class);
            final User user = new User(xmlUser.getValue(), xmlUser.getEmail(), UserFlag.valueOf(xmlUser.getFlag().value()));
            chunk.add(user);
            if (chunk.size() == chunkSize) {
                putToFutures(chunk, futures);
                chunk = new ArrayList<>();
            }
        }
        if (!chunk.isEmpty()) {
            putToFutures(chunk, futures);
        }

        List<FailedEmail> existEmails = new ArrayList<>();
        futures.forEach((k, f) -> {
            try {
                existEmails.addAll(f.get().stream()
                        .map(email->new FailedEmail(email, "already exist"))
                        .collect(Collectors.toList()));
            } catch (InterruptedException | ExecutionException e) {
                existEmails.add(new FailedEmail(k, e.toString()));
            }
        });

        return existEmails;
    }

    private void putToFutures(List<User> chunk, Map<String, Future<List<String>>> futures) {
        String stringChunk = chunk.get(0).getEmail() +
                (chunk.size() > 1 ? " - " + chunk.get(chunk.size() - 1).getEmail() : "");
        futures.put(stringChunk, executorService.submit(()->dao.insertAndGetConflictEmails(chunk)));
    }
}
