package seedu.address.ui;

import java.util.logging.Logger;

import javafx.scene.control.TabPane;
import org.fxmisc.easybind.EasyBind;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.events.ui.JumpToTabRequestEvent;
import seedu.address.commons.events.ui.ParcelPanelSelectionChangedEvent;
import seedu.address.model.parcel.ReadOnlyParcel;

/**
 * Panel containing the list of parcels.
 */
public class ParcelListPanel extends UiPart<Region> {
    private static final String FXML = "ParcelListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ParcelListPanel.class);

    @FXML
    private ListView<ParcelCard> allParcelListView;

    @FXML
    private TabPane tabPanePlaceholder;

    public ParcelListPanel(ObservableList<ReadOnlyParcel> parcelList) {
        super(FXML);
        setConnections(parcelList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<ReadOnlyParcel> parcelList) {
        ObservableList<ParcelCard> mappedList = EasyBind.map(
                parcelList, (parcel) -> new ParcelCard(parcel, parcelList.indexOf(parcel) + 1));
        allParcelListView.setItems(mappedList);
        allParcelListView.setCellFactory(listView -> new ParcelListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        allParcelListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in parcel list panel changed to : '" + newValue + "'");
                        raise(new ParcelPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code ParcelCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            allParcelListView.scrollTo(index);
            allParcelListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    @FXML @Subscribe
    private void handleJumpToTabEvent(JumpToTabRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        tabPanePlaceholder.getSelectionModel().select(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code ParcelCard}.
     */
    class ParcelListViewCell extends ListCell<ParcelCard> {

        @Override
        protected void updateItem(ParcelCard parcel, boolean empty) {
            super.updateItem(parcel, empty);

            if (empty || parcel == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(parcel.getRoot());
            }
        }
    }

}
