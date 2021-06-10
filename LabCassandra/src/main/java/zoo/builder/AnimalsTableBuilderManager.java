package zoo.builder;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.datastax.oss.driver.api.querybuilder.schema.CreateType;
import com.datastax.oss.driver.api.querybuilder.schema.Drop;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.api.querybuilder.update.Update;

public class AnimalsTableBuilderManager extends SimpleManager {

	public AnimalsTableBuilderManager(CqlSession session) {
		super(session);
	}

	public void createTable() {
		CreateType createType = SchemaBuilder.createType("address").withField("region", DataTypes.TEXT)
				.withField("wybieg", DataTypes.INT).withField("cage", DataTypes.INT);

		session.execute(createType.build());

		CreateTable createTable = SchemaBuilder.createTable("animal")
				.withPartitionKey("id", DataTypes.INT)
				.withColumn("names", DataTypes.TEXT)
				.withColumn("species", DataTypes.TEXT)
				.withColumn("age", DataTypes.INT)
				.withColumn("address", QueryBuilder.udt("address"))
				.withColumn("awards", DataTypes.setOf(DataTypes.TEXT));
		session.execute(createTable.build());
	}

	public void insertIntoTable() {
		Insert insert = QueryBuilder.insertInto("zoo", "animal")
				.value("id", QueryBuilder.raw("1"))
				.value("names", QueryBuilder.raw("'Tygrysek'"))
				.value("species", QueryBuilder.raw("'tiger'"))
				.value("age", QueryBuilder.raw("5"))
				.value("address", QueryBuilder.raw("{region : 'Kotowate', wybieg : 1, cage : 1}"))
				.value("awards", QueryBuilder.raw("{'Most popular animal 2020', 'Most popular animal 2019'}"));
		session.execute(insert.build());
		Insert insert2 = QueryBuilder.insertInto("zoo", "animal")
				.value("id", QueryBuilder.raw("2"))
				.value("names", QueryBuilder.raw("'Lewww'"))
				.value("species", QueryBuilder.raw("'lion'"))
				.value("age", QueryBuilder.raw("3"))
				.value("address", QueryBuilder.raw("{region : 'Kotowate', wybieg : 2, cage : 1}"))
				.value("awards", QueryBuilder.raw("{'Cutest animal 2021 January'}"));
		session.execute(insert2.build());
	}
	public void insertIntoTableComplex(Integer id, String name, String species, Integer age, String street, String wybieg,
								String cage, String award) {
		Insert insert = QueryBuilder.insertInto("zoo", "animal")
				.value("id", QueryBuilder.raw(id.toString()))
				.value("names", QueryBuilder.raw("'" + name + "'"))
				.value("species", QueryBuilder.raw("'" + species + "'"))
				.value("age", QueryBuilder.raw(String.valueOf(age)))
				.value("address", QueryBuilder.raw("{ region : " + "'" + street + "'" + ", wybieg : " + Integer.parseInt(wybieg) + ", cage : " + Integer.parseInt(cage) + "}"))
				.value("awards", QueryBuilder.raw("{" + "'" + award + "'" + "}"));
		session.execute(insert.build());
	}

	public void updateIntoTable(int id, int age) {
		Update update = QueryBuilder.update("animal").setColumn("age", QueryBuilder.literal(age)).whereColumn("id").isEqualTo(QueryBuilder.literal(id));
		session.execute(update.build());
	}

	public void deleteFromTable(int id) {
		Delete delete = QueryBuilder.deleteFrom("animal").whereColumn("id").isEqualTo(QueryBuilder.literal(id));
		session.execute(delete.build());
	}

	public void selectFromTable() {
		Select query = QueryBuilder.selectFrom("animal").all();
		SimpleStatement statement = query.build();
		ResultSet resultSet = session.execute(statement);
		for (Row row : resultSet) {
			System.out.print("Animal: ");
			System.out.print(row.getInt("id") + ", ");
			System.out.print(row.get("names", String.class) + ", ");
			System.out.print(row.get("species", String.class) + ", ");
			System.out.print(row.getInt("age") + ", ");
			UdtValue address = row.getUdtValue("address");
			System.out.print("{Sektor: " + address.getString("region") + ", " +"Wybieg: "+ address.getInt("wybieg") + ", "
					+"Klatka: " + address.getInt("cage") + "}" + ", ");
			System.out.print(row.getSet("awards", String.class) + ", ");
			System.out.println();
		}
	}

	public void selectFromTableID(int id){
		Select query = QueryBuilder.selectFrom("animal").all().whereColumn("id").isEqualTo(QueryBuilder.literal(id));
		SimpleStatement statement = query.build();
		ResultSet resultSet = session.execute(statement);
		for (Row row : resultSet) {
			System.out.print("Animal: ");
			System.out.print(row.getInt("id") + ", ");
			System.out.print(row.get("names", String.class) + ", ");
			System.out.print(row.get("species", String.class) + ", ");
			System.out.print(row.getInt("age") + ", ");
			UdtValue address = row.getUdtValue("address");
			System.out.print("{Sektor: " + address.getString("region") + ", " +"Wybieg: "+ address.getInt("wybieg") + ", "
					+"Klatka: " + address.getInt("cage") + "}" + ", ");
			System.out.print(row.getSet("awards", String.class) + ", ");
			System.out.println();
		}
	}

	public void countAverageAge() {
		Select query = QueryBuilder.selectFrom("animal").all();
		SimpleStatement statement = query.build();
		ResultSet resultSet = session.execute(statement);
		int age = 0;
		int i = 0;
		for (Row row : resultSet) {
			age += row.getInt("age");
			i++;
		}
		double average = age / i;
		System.out.println("Average age of animals is: " + average);
	}

	public void dropTable() {
		Drop drop = SchemaBuilder.dropTable("animal");
		executeSimpleStatement(drop.build());
	}


}
