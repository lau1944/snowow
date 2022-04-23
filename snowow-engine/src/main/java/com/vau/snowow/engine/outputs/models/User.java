package com.vau.snowow.engine.outputs.models;
import lombok.*;
@Data() @Setter() @Getter() @Builder() @AllArgsConstructor() @NoArgsConstructor() public class User {	@NonNull() private String name;	private int age;	private School school;}