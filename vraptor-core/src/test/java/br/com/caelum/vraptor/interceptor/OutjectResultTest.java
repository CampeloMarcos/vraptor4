/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.caelum.vraptor.interceptor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.events.OutjectResultEvent;

public class OutjectResultTest {

	private @Mock Result result;
	private @Mock MethodInfo info;
	private @Mock Object instance;
	private @Mock InterceptorStack stack;
	private @Mock TypeNameExtractor extractor;

	private OutjectResult outjectResult;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.outjectResult = new OutjectResult(result, info, extractor);
	}

	interface MyComponent {
		String returnsAString();
		List<String> returnsStrings();
		void noReturn();
	}

	@Test
	public void shouldOutjectWithASimpleTypeName() throws NoSuchMethodException {
		Method method = MyComponent.class.getMethod("returnsAString");
		when(info.getResult()).thenReturn("myString");
		when(extractor.nameFor(String.class)).thenReturn("string");
		outjectResult.outject(new OutjectResultEvent(method.getGenericReturnType()));
		verify(result).include("string", "myString");
	}

	@Test
	public void shouldOutjectACollectionAsAList() throws NoSuchMethodException {
		Method method = MyComponent.class.getMethod("returnsStrings");
		when(info.getResult()).thenReturn("myString");
		when(extractor.nameFor(method.getGenericReturnType())).thenReturn("stringList");
		outjectResult.outject(new OutjectResultEvent(method.getGenericReturnType()));
		verify(result).include("stringList", "myString");
	}
}