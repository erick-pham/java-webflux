package com.example.erick.integration.portcontrol.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateQuoteRequest {

  private Quotation quotation;
  private UserMetadata userMetadata;

  @Data
  @Builder
  public static class Quotation {
    private Product product;
    private List<InsuredPerson> insuredPersons;
  }

  @Data
  @Builder
  public static class Product {
    private String code;
  }

  @Data
  @Builder
  public static class InsuredPerson {
    private String role;
    private PersonalDetails personalDetails;
  }

  @Data
  @Builder
  public static class PersonalDetails {
    private String dob;
    private String gender;
  }

  @Data
  @Builder
  public static class UserMetadata {
    private String tenantId;
    private String channelId;
    private String channelName;
    private String role;
    private String userId;
    private String userPublicId;
  }
}
