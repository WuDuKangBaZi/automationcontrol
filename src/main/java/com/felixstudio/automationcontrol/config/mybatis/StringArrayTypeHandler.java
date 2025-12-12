package com.felixstudio.automationcontrol.config.mybatis;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringArrayTypeHandler implements TypeHandler<List<String>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, java.sql.Types.ARRAY);
            return;
        }
        Array array = ps.getConnection().createArrayOf("text", parameter.toArray(new String[0]));
        ps.setArray(i, array);
    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        Array arr = rs.getArray(columnName);
        if (arr != null) {
            Object raw = arr.getArray();
            if (raw instanceof String[]) {
                return Arrays.asList((String[]) raw);
            }
        }
        String s = rs.getString(columnName);
        return parseBraceArrayString(s);
    }

    @Override
    public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        Array arr = rs.getArray(columnIndex);
        if (arr != null) {
            Object raw = arr.getArray();
            if (raw instanceof String[]) {
                return Arrays.asList((String[]) raw);
            }
        }
        String s = rs.getString(columnIndex);
        return parseBraceArrayString(s);
    }

    @Override
    public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        Array arr = cs.getArray(columnIndex);
        if (arr != null) {
            Object raw = arr.getArray();
            if (raw instanceof String[]) {
                return Arrays.asList((String[]) raw);
            }
        }
        String s = cs.getString(columnIndex);
        return parseBraceArrayString(s);
    }
    private List<String> parseBraceArrayString(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.startsWith("{") && s.endsWith("}")) {
            String inner = s.substring(1, s.length() - 1);
            if (inner.isEmpty()) return Collections.emptyList();
            return Arrays.asList(inner.split(","));
        }
        return Collections.singletonList(s);
    }
}
