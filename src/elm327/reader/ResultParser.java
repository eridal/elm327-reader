package elm327.reader;

import elm327.reader.Results.Response;

class ResultParser {

    public static <T> Result<T> parse(String response, Command<T> command) {
        try {
            return unsafeParse(response, command);
        } catch (Exception e) {
            return new Results.Error<T>(command, e);
        }
    }

    private static <T> Result<T> unsafeParse(String response, Command<T> command) throws ResultException {

        String data = extractData(response, command);

        for (Response r : Response.values()) {
            if (r.matches(data)) {
                return r.createResult(command, data);
            }
        }

        T value = command.parse(data);
        return new Results.Answer<T>(command, value);
    }

    private static <T> String extractData(String response, Command<T> command) throws ResultException {

        String[] fragments = split(response);

        if (fragments.length == 1) {
            return fragments[0];
        }

        if (fragments.length == 2) {
            Message message = command.message();
            if (message.hasCode(fragments[0])) {
                return fragments[1];
            }
        }

        if (fragments.length > 1 && command instanceof MultiFrameCommand) {
            StringBuilder sb = new StringBuilder();
            for (String f : fragments) {
                sb.append(f);
                sb.append('\n');
            }
            return sb.toString();
        }

        throw new ResultException();
    }

    private static String[] split(String response) throws ResultException {

        if (null == response || response.isEmpty()) {
            throw new ResultException();
        }

        response = response.trim();

        if (null == response || response.isEmpty()) {
            throw new ResultException();
        }

        return response.split("\n+");
    }

    @SuppressWarnings("serial")
    static class RetrySignal extends Throwable {

    }
}
