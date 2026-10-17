package utilities;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.invoke.MethodHandles.lookup;

public class CSVFileManager {
    public static final Logger log = LogManager.getLogger(lookup().lookupClass());

    private final String csvFilePath;
    private List<String> columns = new ArrayList<>();
    private List<String[]> rows = new ArrayList<>();

    /**
     * Reads the whole CSV file once into memory.
     * The first line is treated as the header.
     *
     * @param csvFilePath target test data CSV file path
     */
    public CSVFileManager(String csvFilePath) {
        this.csvFilePath = csvFilePath;
        try (CSVReader reader = new CSVReader(
                Files.newBufferedReader(Path.of(csvFilePath),
                        StandardCharsets.UTF_8))) {
            List<String[]> all = reader.readAll();
            all.removeIf(r -> r.length == 1 && r[0].isBlank());
            if (!all.isEmpty()) {
                columns = new ArrayList<>(Arrays.asList(all.get(0)));
                rows = new ArrayList<>(all.subList(1, all.size()));
            }
            log.info("Reading test data from the following file. [{}]", this.csvFilePath);
        } catch (IOException | CsvException e) {
            log.error("Couldn't read the desired file. [{}] ", this.csvFilePath, e);
        }
    }

    /**
     * @return all data rows (without header) as a list of string arrays.
     */
    public List<String[]> getRows() {
        log.info("Successfully retrieved all rows from [{}].", csvFilePath);
        return rows;
    }

    /**
     * @return the column names (header) as a list.
     */
    public List<String> getColumns() {
        log.info("Successfully retrieved column names from [{}].", csvFilePath);
        return new ArrayList<>(columns);
    }

    /**
     * Maps each column name to the list of its values.
     */
    public Map<String, List<String>> getColumnsWithData() {
        Map<String, List<String>> result = new LinkedHashMap<>();
        try {
            for (int i = 0; i < columns.size(); i++) {
                List<String> columnData = new ArrayList<>();
                for (String[] row : rows) {
                    if (i < row.length) {
                        columnData.add(row[i]);
                    }
                }
                result.put(columns.get(i), columnData);
            }
            log.info("Your data had been mapped successfully: {}", result);
        } catch (Exception e) {
            log.error("Error while mapping columns with data: {}", e.getMessage());
        }
        return result;
    }

    /**
     * @return the name of the last column, or null if an error occurs.
     */
    public String getLastColumn() {
        try {
            String last = columns.getLast();
            log.info("The last column had been retrieved successfully : {}", last);
            return last;
        } catch (Exception e) {
            log.error("Error while retrieving the last column: {}", e.getMessage());
            return null;
        }
    }

    /**
     * @return the name of the first column, or null if an error occurs.
     */
    public String getFirstColumn() {
        try {
            String first = columns.getFirst();
            log.info("Successfully retrieved the first column name from [{}]: {}", csvFilePath, first);
            return first;
        } catch (Exception e) {
            log.error("Error while retrieving the first column: {}", e.getMessage());
            return null;
        }
    }

    /**
     * @param columnNum the 1-based index of the column.
     * @return the column name at the specified index, or null if an error occurs.
     */
    public String getSpecificColumnName(int columnNum) {
        try {
            String name = columns.get(columnNum - 1);
            log.info("Column name at index {} is: {}", columnNum, name);
            return name;
        } catch (Exception e) {
            log.error("Error while retrieving column name at index {}: {}", columnNum, e.getMessage());
            return null;
        }
    }

    /**
     * @param columnName the name of the column.
     * @return the column data, or an empty list if the column doesn't exist.
     */
    public List<String> GetSpecificColumnData(String columnName) {
        try {
            List<String> data = getColumnsWithData().get(columnName);
            if (data == null) {
                log.error("Column [{}] not found in [{}]", columnName, csvFilePath);
                return Collections.emptyList();
            }
            log.info("Data of column {}: {}", columnName, data);
            return data;
        } catch (Exception e) {
            log.error("Error while retrieving data for column: {}. {}", columnName, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * @param columnIndex the 1-based index of the column.
     * @return the column data, or an empty list if an error occurs.
     */
    public List<String> GetSpecificColumnData(int columnIndex) {
        try {
            String name = getSpecificColumnName(columnIndex);
            return name == null ? Collections.emptyList() : GetSpecificColumnData(name);
        } catch (Exception e) {
            log.error("Error while retrieving data for columnIndex: {}. {}", columnIndex, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * @param rowNum     the 0-based index of the row (header not counted).
     * @param columnName the name of the column.
     * @return the cell value, or null if an error occurs.
     */
    public String getCellData(int rowNum, String columnName) {
        try {
            int colIndex = columns.indexOf(columnName);
            if (colIndex < 0) {
                throw new IllegalArgumentException("Column not found: " + columnName);
            }
            return rows.get(rowNum)[colIndex];
        } catch (Exception e) {
            log.error("Error while retrieving cell data for Row: {}, Column: {}. {}", rowNum, columnName, e.getMessage());
            return null;
        }
    }

    /**
     * @param rowNum      the 0-based index of the row (header not counted).
     * @param columnIndex the 1-based index of the column.
     * @return the cell value, or null if an error occurs.
     */
    public String getCellData(int rowNum, int columnIndex) {
        try {
            return rows.get(rowNum)[columnIndex - 1];
        } catch (Exception e) {
            log.error("Error while retrieving cell data for Row: {}, ColumnIndex: {}. {}", rowNum, columnIndex, e.getMessage());
            return null;
        }
    }

    /**
     * @param columnName the name of the column.
     * @return the minimum numeric value, or Double.NaN if an error occurs.
     */
    public double getMinCellValue(String columnName) {
        try {
            double min = Double.POSITIVE_INFINITY;
            for (String value : getColumnsWithData().get(columnName)) {
                min = Math.min(min, Double.parseDouble(value.trim()));
            }
            log.info("Min cell value = {} of column: {} from [{}].", min, columnName, csvFilePath);
            return min;
        } catch (Exception e) {
            log.error("Error calculating min value for column: {}. {}", columnName, e.getMessage());
            return Double.NaN;
        }
    }

    /**
     * @param columnIndex the 0-based index of the column.
     */
    public double getMinCellValue(int columnIndex) {
        try {
            return getMinCellValue(columns.get(columnIndex));
        } catch (Exception e) {
            log.error("Error calculating min value for columnIndex: {}. {}", columnIndex, e.getMessage());
            return Double.NaN;
        }
    }

    /**
     * @param columnName the name of the column.
     * @return the maximum numeric value, or Double.NaN if an error occurs.
     */
    public double getMaxCellValue(String columnName) {
        try {
            double max = Double.NEGATIVE_INFINITY;
            for (String value : getColumnsWithData().get(columnName)) {
                max = Math.max(max, Double.parseDouble(value.trim()));
            }
            log.info("Max cell value = {} of column: {} from [{}].", max, columnName, csvFilePath);
            return max;
        } catch (Exception e) {
            log.error("Error calculating max value for column: {}. {}", columnName, e.getMessage());
            return Double.NaN;
        }
    }

    /**
     * @param columnIndex the 0-based index of the column.
     */
    public double getMaxCellValue(int columnIndex) {
        try {
            return getMaxCellValue(columns.get(columnIndex));
        } catch (Exception e) {
            log.error("Error calculating max value for columnIndex: {}. {}", columnIndex, e.getMessage());
            return Double.NaN;
        }
    }

    /**
     * @param columnName the name of the column.
     * @return the number of cells in the column, or 0 if an error occurs.
     */
    public int getCellCount(String columnName) {
        try {
            int count = getColumnsWithData().get(columnName).size();
            log.info("Cell count of column: {} from [{}] = {}", columnName, csvFilePath, count);
            return count;
        } catch (Exception e) {
            log.error("Error calculating count for column: {}. {}", columnName, e.getMessage());
            return 0;
        }
    }

    /**
     * @param columnIndex the 0-based index of the column.
     */
    public int getCellCount(int columnIndex) {
        try {
            return getCellCount(columns.get(columnIndex));
        } catch (Exception e) {
            log.error("Error calculating count for columnIndex: {}. {}", columnIndex, e.getMessage());
            return 0;
        }
    }
}