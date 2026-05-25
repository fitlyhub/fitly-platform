package vn.fitly.infrastructure.query;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import vn.fitly.common.exception.ErrorCode;
import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyRuntimeException;
import vn.fitly.infrastructure.config.ApplicationConfig;
import vn.fitly.infrastructure.datasource.FitlyDbType;
import vn.fitly.infrastructure.query.pg.PgQueryInput;

public class QueryInput {

    private final StringBuilder sb = new StringBuilder();

    private final List<Object> params = new ArrayList<>();

    protected QueryInput() {
    }

    protected void addArrayWhereClause(String columnName, List<? extends Object> paramList, DbObjectType type) {

        if (paramList == null || paramList.isEmpty()) {
            sb.append("\n");
            sb.append("1 = 0");
            return;
        }

        if (paramList.size() > 5000) {
            throw new FitlyRuntimeException(ErrorStatus.INTERNAL_ERROR, ErrorCode.ERROR_WHILE_PROCESSING,
                    "too much parameter on query > " + paramList.size());
        }

        sb.append("\n");
        sb.append(columnName);
        sb.append(" in (");

        boolean isFirst = true;
        for (Object param : paramList) {

            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(",");
            }
            sb.append("?");
            params.add(param);
        }

        sb.append(")");

    }

    public void appendSql(String str) {
        sb.append(str);
    }

    public void addParam(Object param) {
        params.add(param);
    }

    public void addWhereClause(String columnName, Object param) {
        sb.append("\n");
        sb.append(columnName);
        sb.append(" = ?");
        params.add(param);
    }

    public void addArrayWhereClauseInt(String columnName, List<Integer> paramList) {

        addArrayWhereClause(columnName, paramList, DbObjectType.INT4);

    }

    public void addArrayWhereClauseString(String columnName, List<String> paramList) {
        addArrayWhereClause(columnName, paramList, DbObjectType.VARCHAR);

    }

    public void addArrayWhereClauseUUID(String columnName, List<UUID> paramList) {
        addArrayWhereClause(columnName, paramList, DbObjectType.UUID);

    }

    public String getSql() {
        return sb.toString();
    }

    public List<Object> getParams() {
        return this.params;
    }

    public static QueryInput getQueryInput() {

        if (ApplicationConfig.getDatabaseType() == FitlyDbType.POSTGRES) {
            return new PgQueryInput();
        }

        return new QueryInput();

    }

}
