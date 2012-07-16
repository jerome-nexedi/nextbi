package com.tensegrity.wpalo.client.rpc.core.java.util;

import java.util.LinkedHashSet;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.core.java.util.Collection_CustomFieldSerializerBase;

public final class LinkedHashSet_CustomFieldSerializer {

  @SuppressWarnings("unchecked")
  // raw LinkedHashMap
  public static void deserialize(SerializationStreamReader streamReader,
    LinkedHashSet instance) throws SerializationException {
    Collection_CustomFieldSerializerBase.deserialize(streamReader, instance);
  }

  @SuppressWarnings("unchecked")
  // raw LinkedHashMap
  public static LinkedHashSet instantiate(SerializationStreamReader streamReader)
    throws SerializationException {
    return new LinkedHashSet(16, .75f);
  }

  @SuppressWarnings("unchecked")
  // raw LinkedHashMap
  public static void serialize(SerializationStreamWriter streamWriter,
    LinkedHashSet instance) throws SerializationException {
    Collection_CustomFieldSerializerBase.serialize(streamWriter, instance);
  }
}
