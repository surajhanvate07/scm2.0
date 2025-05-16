package com.suraj.scm.helpers;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
	private String content;

	@Builder.Default
	private MessageType type = MessageType.blue;
}
