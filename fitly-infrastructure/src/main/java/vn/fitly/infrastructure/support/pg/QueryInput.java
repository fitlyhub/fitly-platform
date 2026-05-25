package vn.fitly.infrastructure.support.pg;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import vn.fitly.infrastructure.query.pg.PgArrayParameter;

public class QueryInput {

    private final StringBuilder sb = new StringBuilder();

    private final List<Object> params = new ArrayList<>();

    public QueryInput() {
    }

    public void appendSql(String str) {
        sb.append(str);
    }

    public void setParam(Object param) {
        params.add(param);
    }

    public void addWhereClause(String columnName, Object param) {
        sb.append("\n");
        sb.append(columnName);
        sb.append(" = ?");
        params.add(param);
    }

    public void addArrayWhereClauseInt(String columnName, List<Integer> paramList) {

        sb.append("\n");
        sb.append(columnName);
        sb.append(" =  any(?)");
        params.add(PgArrayParameter.setArrayInt(paramList));

    }

    public void addArrayWhereClauseString(String columnName, List<String> paramList) {

        sb.append("\n");
        sb.append(columnName);
        sb.append(" =  any(?)");
        params.add(PgArrayParameter.setArrayString(paramList));

    }

    public void addArrayWhereClauseUUID(String columnName, List<UUID> paramList) {

        sb.append("\n");
        sb.append(columnName);
        sb.append(" =  any(?)");
        params.add(PgArrayParameter.setArrayUUID(paramList));

    }

}
