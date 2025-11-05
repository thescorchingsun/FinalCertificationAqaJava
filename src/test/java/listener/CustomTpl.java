package listener;

import io.qameta.allure.restassured.AllureRestAssured;

/**
 * Возврат нового фильтра AllureRestAssured с кастомными шаблонами
 * для логирования request и response.
 */
public class CustomTpl {

    private CustomTpl() {
    }

    public static AllureRestAssured customLogFilter() {
        AllureRestAssured filter = new AllureRestAssured();
        filter.setRequestTemplate("request.ftl");
        filter.setResponseTemplate("response.ftl");
        return filter;
    }

}
