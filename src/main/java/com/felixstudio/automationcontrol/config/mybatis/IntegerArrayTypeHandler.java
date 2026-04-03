package com.felixstudio.automationcontrol.config.mybatis;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IntegerArrayTypeHandler implements TypeHandler<List<Integer>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, List<Integer> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, java.sql.Types.ARRAY);
            return;
        }
        // PostgreSQL int[] 对应 int4
        Array array = ps.getConnection().createArrayOf("int4", parameter.toArray(new Integer[0]));
        ps.setArray(i, array);
    }

    @Override
    public List<Integer> getResult(ResultSet rs, String columnName) throws SQLException {
        Array arr = rs.getArray(columnName);
        if (arr != null) {
            Object raw = arr.getArray();
            if (raw instanceof Integer[]) {
                Integer[] values = (Integer[]) raw;
                List<Integer> result = new ArrayList<>(values.length);
                Collections.addAll(result, values);
                return result;
            }
            if (raw instanceof Object[]) {
                Object[] values = (Object[]) raw;
                List<Integer> result = new ArrayList<>(values.length);
                for (Object v : values) {
                    if (v != null) {
                        result.add(Integer.valueOf(v.toString()));
                    }
                }
                return result;
            }
        }
        return parseBraceArrayString(rs.getString(columnName));
    }

    @Override
    public List<Integer> getResult(ResultSet rs, int columnIndex) throws SQLException {
        Array arr = rs.getArray(columnIndex);
        if (arr != null) {
            Object raw = arr.getArray();
            if (raw instanceof Integer[]) {
                Integer[] values = (Integer[]) raw;
                List<Integer> result = new ArrayList<>(values.length);
                Collections.addAll(result, values);
                return result;
            }
            if (raw instanceof Object[]) {
                Object[] values = (Object[]) raw;
                List<Integer> result = new ArrayList<>(values.length);
                for (Object v : values) {
                    if (v != null) {
                        result.add(Integer.valueOf(v.toString()));
                    }
                }
                return result;
            }
        }
        return parseBraceArrayString(rs.getString(columnIndex));
    }

    @Override
    public List<Integer> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        Array arr = cs.getArray(columnIndex);
        if (arr != null) {
            Object raw = arr.getArray();
            if (raw instanceof Integer[]) {
                Integer[] values = (Integer[]) raw;
                List<Integer> result = new ArrayList<>(values.length);
                Collections.addAll(result, values);
                return result;
            }
            if (raw instanceof Object[]) {
                Object[] values = (Object[]) raw;
                List<Integer> result = new ArrayList<>(values.length);
                for (Object v : values) {
                    if (v != null) {
                        result.add(Integer.valueOf(v.toString()));
                    }
                }
                return result;
            }
        }
        return parseBraceArrayString(cs.getString(columnIndex));
    }

    private List<Integer> parseBraceArrayString(String s) {
        if (s == null) {
            return null;
        }
        s = s.trim();
        if (s.isEmpty() || "{}".equals(s)) {
            return Collections.emptyList();
        }
        if (s.startsWith("{") && s.endsWith("}")) {
            String inner = s.substring(1, s.length() - 1).trim();
            if (inner.isEmpty()) {
                return Collections.emptyList();
            }
            String[] parts = inner.split(",");
            List<Integer> result = new ArrayList<>(parts.length);
            for (String part : parts) {
                String token = part.trim().replace("\"", "");
                if (!token.isEmpty() && !"NULL".equalsIgnoreCase(token)) {
                    result.add(Integer.valueOf(token));
                }
            }
            return result;
        }
        return Collections.singletonList(Integer.valueOf(s));
    }
}
