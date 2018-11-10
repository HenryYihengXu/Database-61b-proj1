package db61b;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/** The suite of all JUnit tests for the qirkat package.
 *  @author P. N. Hilfinger
 */
public class UnitTest {

    /** Testing methods in Table such as columns(),
     *  size(), findColumn(), add(), etc. */
    @Test
    public void testTable() {
        String[] columnTitles = {"SID", "CCN", "Grade"};
        Table table1 = new Table(columnTitles);
        String[] actual = new String[3];
        for (int i = 0; i < 3; i++) {
            actual[i] = table1.getTitle(i);
        }
        assertArrayEquals(columnTitles, actual);
        assertEquals(3, table1.columns());
        assertEquals(-1, table1.findColumn("Sid"));
        assertEquals(2, table1.findColumn("Grade"));
        assertEquals(0, table1.size());

        String[] values1 = {"101", "21228", "B"};
        assertEquals(true, table1.add(values1));
        assertEquals(1, table1.size());
        assertEquals("B", table1.get(0, 2));
        assertEquals(false, table1.add(values1));
        String[] values2 = {"101", "21105", "B+"};
        assertEquals(true, table1.add(values2));
        assertEquals("B+", table1.get(1, 2));
    }

    /** Testing the second add().*/
    @Test
    public void testAdd2() {
        String[] columnTitles1 = {"SID", "CCN", "Grade"};
        String[] columnTitles2 = {"sid", "cnn", "grade"};
        String[] columnTitles3 = {"1", "2", "3"};
        String[] values1 = {"101", "21228", "B"};
        String[] values2 = {"101", "21105", "B+"};
        Table table1 = new Table(columnTitles1);
        Table table2 = new Table(columnTitles2);
        Table table3 = new Table(columnTitles3);
        table1.add(values1);
        table1.add(values2);
        table2.add(values1);
        table3.add(values1);

        Column column1 = new Column("Grade", table1, table2, table3);
        Column column2 = new Column("cnn", table1, table2, table3);
        Column column3 = new Column("1", table1, table2, table3);
        List<Column> columns = new ArrayList<>();
        columns.add(column1);
        columns.add(column2);
        columns.add(column3);
        table2.add(columns, 1, 0, 0);
        assertEquals("B+", table2.get(1, 0));
    }

    /** Testing readTable().*/
    @Test
    public void testReadTable() {
        Table table1 = Table.readTable("enrolled");
        assertEquals("CCN", table1.getTitle(1));
        assertEquals("21105", table1.get(1, 1));
    }

    /** Testing writeTable().*/
    @Test
    public void testWriteTable() {
        Table table1 = Table.readTable("enrolled");
        table1.writeTable("test");
    }

    /** Testing print().*/
    @Test
    public void testPrint() {
        Table table1 = Table.readTable("enrolled");
        table1.print();
    }

    /** Testing methods in Database.*/
    @Test
    public void testDatabase() {
        Table table1 = Table.readTable("enrolled");
        Database db = new Database();
        db.put("enrolled", table1);
        Table table2 = db.get("enrolled");
        assertEquals(table2, table1);
    }

    /** Testing add() and if _index is correctly changed.*/
    @Test
    public void testSortedAdd() {
        String[] columnTitles = {"number"};
        Table table1 = new Table(columnTitles);
        String[] values1 = {"4"};
        String[] values2 = {"3"};
        String[] values3 = {"1"};
        String[] values4 = {"2"};
        table1.add(values1);
        table1.add(values2);
        table1.add(values3);
        table1.add(values4);
        List<Integer> expected = new ArrayList<Integer>();
        expected.add(2);
        expected.add(3);
        expected.add(1);
        expected.add(0);
        assertEquals(expected, table1.getIndex());
    }

    /** Testing Condition.*/
    @Test
    public void testCondition() {
        Table table1 = Table.readTable("enrolled");
        Column column1 = new Column("SID", table1);
        Column column2 = new Column("CCN", table1);
        Condition condition1 = new Condition(column1, "<", column2);
        assertEquals(true, condition1.test(0));
    }

    /** Testing the first select().*/
    @Test
    public void testSelect1() {
        Table table1 = Table.readTable("enrolled");
        Column column1 = new Column("SID", table1);
        Condition condition1 = new Condition(column1, "=", "101");
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition1);
        List<String> columnNames = new ArrayList<>();
        columnNames.add("SID"); columnNames.add("CCN");
        table1.select(columnNames, conditions).print();
    }

    /** Testing the second select().*/
    @Test
    public void testSelect2() {
        Table table1 = Table.readTable("enrolled");
        Table table2 = Table.readTable("students");
        Column column1 = new Column("CCN", table1);
        Condition condition1 = new Condition(column1, "=", "21001");
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition1);
        List<String> columnNames1 = new ArrayList<>();
        columnNames1.add("Firstname");
        columnNames1.add("Lastname");
        columnNames1.add("Grade");
        table1.select(table2, columnNames1, conditions).print();
    }

    /** An overall test.*/
    @Test
    public void testAll() {
        Table students = Table.readTable("students");
        Table enrolled = Table.readTable("enrolled");
        Table schedule = Table.readTable("schedule");

        Column column1 = new Column("Lastname", students);
        Condition condition1 = new Condition(column1, "=", "Chan");
        List<Condition> conditions1 = new ArrayList<>();
        conditions1.add(condition1);
        List<String> columnNames1 = new ArrayList<>();
        columnNames1.add("SID"); columnNames1.add("Firstname");
        students.select(columnNames1, conditions1).print();

        Column column2 = new Column("CCN", enrolled);
        Condition condition2 = new Condition(column2, "=", "21001");
        List<Condition> conditions2 = new ArrayList<>();
        conditions2.add(condition2);
        List<String> columnNames2 = new ArrayList<>();
        columnNames2.add("Firstname");
        columnNames2.add("Lastname");
        columnNames2.add("Grade");
        enrolled.select(students, columnNames2, conditions2).print();
    }
    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        System.exit(ucb.junit.textui.runClasses(UnitTest.class));
    }

}
