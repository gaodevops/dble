package io.mycat.backend.mysql.nio.handler.builder;

import java.sql.SQLNonTransientException;
import java.sql.SQLSyntaxErrorException;

import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;

import io.mycat.MycatServer;
import io.mycat.backend.mysql.nio.handler.builder.BaseHandlerBuilder.MySQLNodeType;
import io.mycat.backend.mysql.nio.handler.builder.sqlvisitor.PushDownVisitor;
import io.mycat.cache.LayerCachePool;
import io.mycat.config.MycatConfig;
import io.mycat.config.model.SchemaConfig;
import io.mycat.plan.PlanNode;
import io.mycat.route.RouteResultset;
import io.mycat.route.RouteResultsetNode;
import io.mycat.route.parser.druid.DruidParser;
import io.mycat.route.parser.druid.MycatSchemaStatVisitor;
import io.mycat.route.parser.druid.impl.DruidBaseSelectParser;
import io.mycat.route.util.RouterUtil;
import io.mycat.server.NonBlockingSession;
import io.mycat.server.parser.ServerParse;

public class MergeBuilder {
	private boolean simpleVisited;
	private boolean needCommonFlag;
	private boolean needSendMakerFlag;
	private PlanNode node;
	private NonBlockingSession session;
	private MySQLNodeType nodeType;
	private String schema;
	private MycatConfig mycatConfig;
	private PushDownVisitor pdVisitor;

	public MergeBuilder(NonBlockingSession session, PlanNode node, boolean needCommon, boolean needSendMaker,
			PushDownVisitor pdVisitor) {
		this.node = node;
		this.simpleVisited = false;
		this.needCommonFlag = needCommon;
		this.needSendMakerFlag = needSendMaker;
		this.session = session;
		this.schema = session.getSource().getSchema();
		this.nodeType = session.getSource().isTxstart() || !session.getSource().isAutocommit() ? MySQLNodeType.MASTER
				: MySQLNodeType.SLAVE;
		this.mycatConfig = MycatServer.getInstance().getConfig();
		this.pdVisitor = pdVisitor;
	}

	/**
	 * 将一个或者多个条件合并后计算出所需要的节点...
	 * 
	 * @return
	 * @throws SQLNonTransientException 
	 * @throws SQLSyntaxErrorException
	 */
	public RouteResultsetNode[] construct() throws SQLNonTransientException  {
		pdVisitor.visit();
		String sql = pdVisitor.getSql().toString();
		SQLStatementParser parser = new MySqlStatementParser(sql);
		SQLSelectStatement select = (SQLSelectStatement) parser.parseStatement();
		MycatSchemaStatVisitor visitor = new MycatSchemaStatVisitor();
		DruidParser druidParser = new DruidBaseSelectParser();

		RouteResultset rrs = new RouteResultset(sql, ServerParse.SELECT, null);
		LayerCachePool pool = MycatServer.getInstance().getRouterservice().getTableId2DataNodeCache();
		SchemaConfig schemaConfig = mycatConfig.getSchemas().get(schema);
		rrs = RouterUtil.routeFromParser(druidParser, schemaConfig, rrs, select, sql, pool, visitor);

		return rrs.getNodes();
	}

	/* -------------------- getter/setter -------------------- */
	public boolean getNeedCommonFlag() {
		return needCommonFlag;
	}

	public boolean isSimpleVisited() {
		return simpleVisited;
	}

	public boolean getNeedSendMakerFlag() {
		return needSendMakerFlag;
	}

	public void setNodeType(MySQLNodeType nodeType) {
		this.nodeType = nodeType;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

}
