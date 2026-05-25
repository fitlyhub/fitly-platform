package vn.fitly.infrastructure.query.pg;

import java.util.List;

import vn.fitly.infrastructure.query.QueryInput;
import vn.fitly.infrastructure.query.DbObjectType;

public class PgQueryInput extends QueryInput {

    @Override
    protected void addArrayWhereClause(String columnName, List<? extends Object> paramList, DbObjectType type) {
        if (paramList == null || paramList.isEmpty()) {
            appendSql("\n");
            appendSql("1 = 0");
            return;
        }

        appendSql("\n");
        appendSql(columnName);
        appendSql(" = any(?)");
        addParam(new PgArrayParameter(type, paramList));
    }

}
