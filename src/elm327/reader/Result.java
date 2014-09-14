package elm327.reader;

interface Result<T> {

    T getData();

    class Response<T> implements Result<T> {
        private final Command<T> command;
        private final T data;

        Response(Command<T> command, String data) {
            this.command = command;
            this.data = command.parse(data);
        }

        @Override public T getData() {
            return data;
        }

        public Command<T> getCommand() {
            return command;
        }
    }

    class Error<T> implements Result<T> {

        public final Exception error;

        public Error(Exception error) {
            this.error = error;
        }

        @Override public T getData() {
            return null;
        }

        public Exception getError() {
            return error;
        }
    }
}
