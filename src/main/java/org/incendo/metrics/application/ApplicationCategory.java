package org.incendo.metrics.application;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ApplicationCategory
{

	@Getter
	private final String key;

}
