//
// MessagePack for Java
//
// Copyright (C) 2009-2010 FURUHASHI Sadayuki
//
//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//
package org.msgpack.template;

import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.io.IOException;
import org.msgpack.*;

public class CollectionTemplate implements Template {
	private Template elementTemplate;

	public CollectionTemplate(Template elementTemplate) {
		this.elementTemplate = elementTemplate;
	}

	public void pack(Packer pk, Object target) throws IOException {
		if(!(target instanceof Collection)) {
			throw new MessageTypeException();
		}
		Collection<Object> collection = (Collection<Object>)target;
		pk.packArray(collection.size());
		for(Object element : collection) {
			elementTemplate.pack(pk, element);
		}
	}

	public Object unpack(Unpacker pac) throws IOException, MessageTypeException {
		int length = pac.unpackArray();
		List<Object> list = new LinkedList<Object>();
		for(; length > 0; length--) {
			list.add( elementTemplate.unpack(pac) );
		}
		return list;
	}

	public Object convert(MessagePackObject from) throws MessageTypeException {
		MessagePackObject[] array = from.asArray();
		List<Object> list = new LinkedList<Object>();
		for(MessagePackObject element : array) {
			list.add( elementTemplate.convert(element) );
		}
		return list;
	}
}

