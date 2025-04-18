package helper;

public class UniqueId {
    private int nextNewProjectId;
    private int nextNewApplicationId;
    private int nextNewRegistrationId;
    private int nextEnquiryId;
    private int nextReplyId;

    public UniqueId() {
        nextNewProjectId = 1;
        nextNewApplicationId = 1;
        nextNewRegistrationId = 1;
        nextEnquiryId = 1;
        nextReplyId = 1;
    }

    public int getNextProjectId() {
        return nextNewProjectId++;
    }

    public int getNextApplicationId() {
        return nextNewApplicationId++;
    }

    public int getNextRegistrationId() {
        return nextNewRegistrationId++;
    }

    public int getNextEnquiryId() {
        return nextEnquiryId++;
    }

    public int getNextReplyId() {
        return nextReplyId++;
    }
}
