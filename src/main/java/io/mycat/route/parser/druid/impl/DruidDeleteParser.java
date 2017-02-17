package io.mycat.route.parser.druid.impl;

import java.sql.SQLNonTransientException;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;

import io.mycat.config.MycatPrivileges;
import io.mycat.config.MycatPrivileges.Checktype;
import io.mycat.config.model.SchemaConfig;
import io.mycat.route.RouteResultset;
import io.mycat.route.parser.druid.MycatSchemaStatVisitor;
import io.mycat.route.util.RouterUtil;
import io.mycat.server.util.SchemaUtil;
import io.mycat.server.util.SchemaUtil.SchemaInfo;

/**
 * see http://dev.mysql.com/doc/refman/5.7/en/delete.html
 *
 * @author huqing.yan
 *
 */
public class DruidDeleteParser extends DefaultDruidParser {
	@Override
	public SchemaConfig visitorParse(SchemaConfig schema, RouteResultset rrs, SQLStatement stmt, MycatSchemaStatVisitor visitor)
			throws SQLNonTransientException {
		String schemaName = schema == null ? null : schema.getName();
		MySqlDeleteStatement delete = (MySqlDeleteStatement) stmt;
		SQLTableSource tableSource = delete.getTableSource();
		SQLTableSource fromSource = delete.getFrom();
		if (fromSource != null) {
			tableSource = fromSource;
		}
		if (tableSource instanceof SQLJoinTableSource) {
			SchemaInfo schemaInfo = SchemaUtil.isNoSharding(schemaName, (SQLJoinTableSource) tableSource, stmt);
			if (schemaInfo == null) {
				String msg = "deleting from multiple tables is not supported, sql:" + stmt;
				throw new SQLNonTransientException(msg);
			} else {
				rrs.setStatement(RouterUtil.removeSchema(rrs.getStatement(), schemaInfo.schema));
				if(!MycatPrivileges.checkPrivilege(rrs, schemaInfo.schema, schemaInfo.table, Checktype.DELETE)){
					String msg = "The statement DML privilege check is not passed, sql:" + stmt;
					throw new SQLNonTransientException(msg);
				}
				super.visitorParse(schema, rrs, stmt, visitor);
				RouterUtil.routeForTableMeta(rrs, schemaInfo.schemaConfig, schemaInfo.table, rrs.getStatement());
				rrs.setFinishedRoute(true);
			}
		} else {
			SchemaInfo schemaInfo = SchemaUtil.getSchemaInfo(schemaName, (SQLExprTableSource) tableSource);
			if (schemaInfo == null) {
				String msg = "No MyCAT Database is selected Or defined, sql:" + stmt;
				throw new SQLNonTransientException(msg);
			}
			rrs.setStatement(RouterUtil.removeSchema(rrs.getStatement(), schemaInfo.schema));
			if(!MycatPrivileges.checkPrivilege(rrs, schemaInfo.schema, schemaInfo.table, Checktype.DELETE)){
				String msg = "The statement DML privilege check is not passed, sql:" + stmt;
				throw new SQLNonTransientException(msg);
			}
			schema = schemaInfo.schemaConfig;
			ctx.addTable(schemaInfo.table);
			ctx.setSql(RouterUtil.getFixedSql(RouterUtil.removeSchema(ctx.getSql(),schemaInfo.schema)));
			super.visitorParse(schema, rrs, stmt, visitor);
		}
		return schema;
	}
}
