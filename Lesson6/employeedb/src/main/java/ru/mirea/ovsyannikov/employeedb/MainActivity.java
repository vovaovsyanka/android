package ru.mirea.ovsyannikov.employeedb;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView employeesListView;
    private EmployeeDao employeeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        employeesListView = findViewById(R.id.employeesListView);
        Button showAllButton = findViewById(R.id.showAllButton);
        Button addButton = findViewById(R.id.addButton);

        employeeDao = App.getInstance().getDatabase().employeeDao();

        showAllButton.setOnClickListener(v -> {
            List<Employee> employees = employeeDao.getAll();
            showEmployees(employees);
        });

        addButton.setOnClickListener(v -> showAddEmployeeDialog());

        showAllButton.performClick();
    }

    private void showEmployees(List<Employee> employees) {
        List<String> employeeStrings = new ArrayList<>();
        for (Employee employee : employees) {
            employeeStrings.add(String.format("%s - %d$ - %s",
                    employee.name, employee.salary, employee.superpower));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, employeeStrings);
        employeesListView.setAdapter(adapter);
    }

    private void showAddEmployeeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Добавить супергероя");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_employee, null);
        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        final EditText salaryEditText = view.findViewById(R.id.salaryEditText);
        final EditText superpowerEditText = view.findViewById(R.id.superpowerEditText);

        builder.setView(view);
        builder.setPositiveButton("Добавить", (dialog, which) -> {
            String name = nameEditText.getText().toString();
            int salary = Integer.parseInt(salaryEditText.getText().toString());
            String superpower = superpowerEditText.getText().toString();

            Employee employee = new Employee(name, salary, superpower);
            employeeDao.insert(employee);

            showEmployees(employeeDao.getAll());
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }
}