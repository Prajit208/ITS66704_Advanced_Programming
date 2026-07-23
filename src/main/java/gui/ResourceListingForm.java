package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ResourceListingForm {

    @FXML private ComboBox<String> typeFilter;
    @FXML private TextField locationFilter;
    @FXML private CheckBox availableOnlyCheck;
    @FXML private TableView<?> resourceTable;
    @FXML private TableColumn<?, ?> colResourceId;
    @FXML private TableColumn<?, ?> colType;
    @FXML private TableColumn<?, ?> colLocation;
    @FXML private TableColumn<?, ?> colCapacity;
    @FXML private TableColumn<?, ?> colAvailability;
    @FXML private TableColumn<?, ?> colRules;

    @FXML private Button btnBook;
    @FXML private Button btnMyBookings;


    private String currentUserRole = "STUDENT";

    @FXML
    public void initialize() {
        typeFilter.getItems().addAll("Study Room", "Lab", "Equipment", "Event Space");
    }

    //Setter method to get role of user
    public void setCurrentUserRole(String role){
        this.currentUserRole=role;
        roleCheck(); // if user is STAFF, move buttons are visible to them

    }

    private void roleCheck() {
        if (currentUserRole.equals("STAFF")) {
            btnMyBookings.setText("Manage My Bookings");
        }
    }

    @FXML
    private void handleSearch() {
        String type = typeFilter.getValue();
        String location = locationFilter.getText();
        boolean availableOnly = availableOnlyCheck.isSelected();

        // placeholder: ResourceManager.searchAvailable(type, location, availableOnly, null)
        // then populate resourceTable with the results
    }

    @FXML
    private void handleBookResource() {
        // get selected row from resourceTable, open BookingForm with that resource
    }

    @FXML
    private void handleMyBookings() {
        //BookingManager.getBookingsForUser(currentUserId)
    }

    @FXML
    private void handleBack() {
        //return to previous dashboard/screen
    }
}