package listener;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class AllureLogsExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {
        context.getExecutionException().ifPresent(x -> {
            AllureLogsAttachment.pageSource();
            AllureLogsAttachment.pageScreen();
            AllureLogsAttachment.getLogs();
        });
    }

}
