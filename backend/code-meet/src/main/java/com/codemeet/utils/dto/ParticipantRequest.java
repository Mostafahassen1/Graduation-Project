package com.codemeet.utils.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * This DTO is used as a generic request coming from<br/>
 * a participant. It can be used for:
 * <ul>
 *  <li>Asking to join a meeting</li>
 *  <li>Asking to close a meeting</li>
 *  <li>Asking to leave the meeting</li>
 * </ul>
 * @param username
 * @param meetingId
 */
public record ParticipantRequest(
    @NotNull
    @NotBlank
    String username,

    @NotNull
    Integer meetingId
) {
}
