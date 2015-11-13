package enno.operation.zkException;

/**
 * Created by v-zoli on 2015/11/13.
 */
public class InitializeSchemaFailedException extends Exception {
    public InitializeSchemaFailedException() {
        super();
    }

    public InitializeSchemaFailedException(String msg) {
        super(msg);
    }

    public InitializeSchemaFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InitializeSchemaFailedException(Throwable cause) {
        super(cause);
    }
}
