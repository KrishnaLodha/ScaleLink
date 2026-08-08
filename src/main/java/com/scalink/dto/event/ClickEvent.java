package com.scalink.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClickEvent {

    private Long urlId;
    private String country;
    private String browser;
    private String device;
    private String operatingSystem;
    private String referrer;
    private String ipHash;
}
