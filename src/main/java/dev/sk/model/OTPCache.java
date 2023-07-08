package dev.sk.model;

import java.time.LocalDateTime;

public record OTPCache( String otp, LocalDateTime expiry) {

}
