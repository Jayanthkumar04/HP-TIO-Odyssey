package com.ascendion.agentflowmind.util;

import java.util.Base64;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtDecoder {
	public static Map<String, Object> decodePayload(String token) throws Exception {
		String[] parts = token.split("\\.");
		if (parts.length < 2)
			throw new IllegalArgumentException("Invalid JWT");

		String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(payload, new TypeReference<>() {
		});
	}
}
