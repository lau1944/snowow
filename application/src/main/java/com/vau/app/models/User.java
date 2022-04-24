package com.vau.app.models;
import lombok.*;
@Data() @Setter() @Getter() @Builder() @AllArgsConstructor() @NoArgsConstructor() public class User {	@NonNull() private String name;	private int age;	private School school;}