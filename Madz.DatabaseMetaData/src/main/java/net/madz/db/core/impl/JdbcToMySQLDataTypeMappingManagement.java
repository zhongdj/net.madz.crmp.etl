package net.madz.db.core.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;

import net.madz.db.configuration.JdbcDataTypeBinding;
import net.madz.db.configuration.JdbcDataTypeBindings;
import net.madz.db.configuration.PropertyBinding;
import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.datatype.DataType;
import net.madz.db.utils.MessageConsts;
import net.madz.db.utils.Utilities;

import org.xml.sax.SAXException;

public class JdbcToMySQLDataTypeMappingManagement {

    private static JdbcToMySQLDataTypeMappingManagement instance = new JdbcToMySQLDataTypeMappingManagement();
    final private static Map<Integer, List<JdbcDataTypeBinding>> allDataTypeBinding = new HashMap<Integer, List<JdbcDataTypeBinding>>();
    private static JdbcDataTypeBindings jdbcDataTypeBindings;
    private static String configurationFilePath = "./conf/JdbcDataTypeToMySqlMapping.xml";
    static {
        loadConfiguration();
    }

    private JdbcToMySQLDataTypeMappingManagement() {
    }

    private static void loadConfiguration() {
        InputStream resource = null;
        try {
            final JAXBContext context = JAXBContext.newInstance(JdbcDataTypeBindings.class);
            final Unmarshaller shaller = context.createUnmarshaller();
            shaller.setSchema(SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File("./conf/JdbcDataTypeToMySqlMappingSchema.xsd")));
            resource = new FileInputStream(configurationFilePath);
            jdbcDataTypeBindings = (JdbcDataTypeBindings) shaller.unmarshal(resource);
            final List<JdbcDataTypeBinding> jdbcDataTypeBindingList = jdbcDataTypeBindings.getJdbcDataTypeBinding();
            for ( JdbcDataTypeBinding jdbcBinding : jdbcDataTypeBindingList ) {
                if ( allDataTypeBinding.containsKey(jdbcBinding.getId()) ) {
                    allDataTypeBinding.get(jdbcBinding.getId()).add(jdbcBinding);
                } else {
                    final List<JdbcDataTypeBinding> jdbcDataTypeBindings = new LinkedList<JdbcDataTypeBinding>();
                    jdbcDataTypeBindings.add(jdbcBinding);
                    allDataTypeBinding.put(jdbcBinding.getId(), jdbcDataTypeBindings);
                }
            }
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        } catch (SAXException e) {
            throw new IllegalStateException(e);
        } finally {
            if ( null != resource ) {
                try {
                    resource.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public List<JdbcDataTypeBinding> getJdbcDataTypeBinding(int id) {
        final List<JdbcDataTypeBinding> jdbcDataTypeBinding = allDataTypeBinding.get(id);
        if ( null == jdbcDataTypeBinding ) {
            throw new IllegalArgumentException(MessageConsts.THE_DATA_TYPE_NOT_SUPPORT + id);
        }
        return jdbcDataTypeBinding;
    }

    public static JdbcToMySQLDataTypeMappingManagement getInstance() {
        return instance;
    }

    public <T extends DataType> T getMySQLDataType(JdbcColumnMetaData column) {
        T result = null;
        Utilities.validateInputValueNotNull(column);
        final Integer sqlType = column.getSqlType();
        final List<JdbcDataTypeBinding> bindings = getJdbcDataTypeBinding(sqlType);
        if ( null == bindings ) {
            throw new IllegalArgumentException(MessageConsts.THE_DATA_TYPE_NOT_SUPPORT + ", the data type value is" + sqlType);
        }
        JdbcDataTypeBinding binding = null;
        for ( JdbcDataTypeBinding item : bindings ) {
            final Integer maxLength = item.getMaxLength();
            if ( null == maxLength || column.getSize() < maxLength ) {
                binding = item;
            }
        }
        if ( null == binding ) {
            throw new IllegalStateException(MessageConsts.THE_DATA_TYPE_NOT_SUPPORT + column.getSqlTypeName());
        }
        final String dataTypeClass = binding.getDataTypeClass();
        if ( null != dataTypeClass ) {
            final List<PropertyBinding> propertyBindingList = binding.getPropertyBinding();
            try {
                if ( propertyBindingList.size() <= 0 ) {
                    result = (T) Class.forName(dataTypeClass).newInstance();
                } else {
                    final Class<?> clzInstance = Class.forName(dataTypeClass);
                    Class[] argumentTypes = new Class[propertyBindingList.size()];
                    List argumentValues = new LinkedList();
                    for ( int i = 0; i < propertyBindingList.size(); i++ ) {
                        argumentTypes[i] = Class.forName(propertyBindingList.get(i).getConstructorArgumentType());
                        final String jdbcName = propertyBindingList.get(i).getJdbcName();
                        final String getJdbcNameMethodName = "get" + jdbcName.substring(0, 1).toUpperCase() + jdbcName.substring(1);
                        Object value = column.getClass().getMethod(getJdbcNameMethodName, null).invoke(column, null);
                        final String datatypeConvertorClzName = propertyBindingList.get(i).getDatatypeConvertorClzName();
                        if ( null != datatypeConvertorClzName ) {
                            Class<?> clz = Class.forName(datatypeConvertorClzName);
                            Object newInstance = clz.newInstance();
                            Method method = clz.getMethod("convert", value.getClass());
                            value = method.invoke(newInstance, value);
                        }
                        argumentValues.add(value);
                    }
                    final Constructor<?> constructor = clzInstance.getConstructor(argumentTypes);
                    result = (T) constructor.newInstance(argumentValues.toArray());
                }
            } catch (InstantiationException e) {
                throw new IllegalStateException(e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException(e);
            } catch (SecurityException e) {
                throw new IllegalStateException(e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException(e);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(e);
            }
        }
        return result;
    }
}
