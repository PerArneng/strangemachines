package com.scalebit.strangemachines;


public class GuardResponse {

    private final GuardResponseStatus status;
    private final String message;

    public GuardResponse(GuardResponseStatus status) {
        this(status, status.name());
    }

    public GuardResponse(GuardResponseStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean hasStatus(GuardResponseStatus askStatus) {
        return status == askStatus;
    }

    public GuardResponseStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
