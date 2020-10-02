package restaurant.exceptions;

public class NonPositiveArgumentException extends Exception {
        private final int argument;

        public NonPositiveArgumentException(int argument) {
                this.argument = argument;
        }

        public int getArgument() {
                return argument;
        }
}
