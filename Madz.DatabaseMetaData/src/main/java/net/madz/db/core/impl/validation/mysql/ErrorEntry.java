package net.madz.db.core.impl.validation.mysql;

import net.madz.db.core.meta.DottedPath;

public class ErrorEntry {

    private final DottedPath path;
    private final int errorCode;
    private final String item;
    private final String expectedValue;
    private final String actualValue;
    private final String errorMessage;

    public int getErrorCode() {
        return errorCode;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public String getActualValue() {
        return actualValue;
    }

    public DottedPath getPath() {
        return path;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getItem() {
        return item;
    }

    public ErrorEntry(DottedPath path, String item, int errorCode, String expectedValue, String actualValue) {
        super();
        this.path = path;
        this.item = item;
        this.errorCode = errorCode;
        this.errorMessage = ErrorCodeTypes.getErrorCodeMessage(errorCode);
        this.expectedValue = expectedValue;
        this.actualValue = actualValue;
    }

    public ErrorEntry(DottedPath path, String item, int errorCode, int expectedValue, int actualValue) {
        this(path, item, errorCode, String.valueOf(expectedValue), String.valueOf(actualValue));
    }

    @Override
    public String toString() {
        return "ErrorEntry [path={" + path + "}, item={" + item + "}, errorCode={" + errorCode + "}, expectedValue={" + expectedValue + "}, actualValue={"
                + actualValue + "}, errorMessage={" + errorMessage + "}]";
    }
}
