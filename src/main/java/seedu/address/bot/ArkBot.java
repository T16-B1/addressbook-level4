//@@author fustilio
package seedu.address.bot;

import static org.telegram.abilitybots.api.objects.Flag.PHOTO;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.File;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.google.common.annotations.VisibleForTesting;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import seedu.address.bot.parcel.DisplayParcel;
import seedu.address.bot.parcel.ParcelParser;
import seedu.address.bot.qrcode.QRcodeAnalyser;
import seedu.address.bot.qrcode.exceptions.QRreadException;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.parcel.ReadOnlyParcel;

/**
 * Arkbot contains all of the commands available for use.
 */
public class ArkBot extends AbilityBot {

    public static final String BOT_MESSAGE_FAILURE = "Oh dear, something went wrong! Please try again!";
    public static final String BOT_MESSAGE_SUCCESS = "%s command has been successfully executed!";
    public static final String BOT_MESSAGE_START = "Welcome to ArkBot, your friendly companion to ArkBot on Desktop.\n"
                                                 + "Over here, you can interface with your Desktop application with "
                                                 + "the following functions /add, /list, /delete, /undo, /redo, "
                                                 + "/complete, /cancel and /help.";
    public static final String BOT_MESSAGE_COMPLETE_COMMAND = "Please upload QR code to complete delivery.\n"
                                                            + "Type \"/cancel\" to stop uploading process.";
    public static final String BOT_MESSAGE_CANCEL_COMMAND = "QR Code upload successfully cancelled!";
    public static final String BOT_MESSAGE_HELP = "The commands available to ArkBot v1.5 are as follows: \n"
                                                + "/all Parcel Details - Adds a parcel.\n"
                                                + "/list - Lists uncompleted parcel deliveries.\n"
                                                + "/delete Parcel Index - Deletes a parcel.\n"
                                                + "/undo - Undo a command.\n"
                                                + "/redo - Redo a command.\n"
                                                + "/complete Parcel Index - Marks a parcel as completed.\n"
                                                + "/complete - Activates `listen` mode.\n"
                                                + "/cancel - Cancels `listen` mode.\n"
                                                + "/help - Brings up this dialogue again.\n\n"
                                                + "In `listen` mode, ArkBot will wait for a QR code of a parcel "
                                                + "to be marked as completed. Otherwise, ArkBot "
                                                + "will return the details of the parcel embedded in the QR code.\n\n"
                                                + "Refer to our [User Guide](https://github.com/CS2103AUG2017-T16-B1"
                                                + "/main/blob/master/docs/UserGuide.adoc) for more information.";
    private static final String BOT_SET_COMPLETED = "s/Completed";
    private static final Privacy PRIVACY_SETTING = Privacy.PUBLIC;

    private static final Logger logger = LogsCenter.getLogger(ArkBot.class);

    private Logic logic;
    private Model model;
    private boolean waitingForImage;

    public ArkBot(Logic logic, Model model, String botToken, String botUsername) {
        super(botToken, botUsername);
        this.logic = logic;
        this.model = model;
        this.waitingForImage = false;
        logger.info("ArkBot successfully booted up.");
    }

    /**
     * Replicates the effects of AddCommand on ArkBot.
     */
    public Ability startCommand() {
        return Ability
                .builder()
                .name("start")
                .info("welcomes the user to ArkBot")
                .input(0)
                .locality(Locality.ALL)
                .privacy(PRIVACY_SETTING)
                .action(ctx -> Platform.runLater(() -> sender.send(BOT_MESSAGE_START, ctx.chatId())))
                .build();
    }

    /**
     * Replicates the effects of AddCommand on ArkBot.
     */
    public Ability addCommand() {
        return Ability
                .builder()
                .name(AddCommand.COMMAND_WORD)
                .info("adds parcel to list")
                .input(0)
                .locality(Locality.ALL)
                .privacy(PRIVACY_SETTING)
                .action(ctx -> Platform.runLater(() -> {
                    try {
                        logic.execute(AddCommand.COMMAND_WORD + " "
                                + combineArguments(ctx.arguments()));
                        sender.send(String.format(BOT_MESSAGE_SUCCESS, AddCommand.COMMAND_WORD), ctx.chatId());
                    } catch (CommandException | ParseException e) {
                        sender.send(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE),
                                ctx.chatId());
                    }
                }))
                .build();
    }

    /**
     * Replicates the effects of ListCommand on ArkBot.
     */
    public Ability listCommand() {
        return Ability
                .builder()
                .name(ListCommand.COMMAND_WORD)
                .info("lists all parcels")
                .input(0)
                .locality(Locality.ALL)
                .privacy(PRIVACY_SETTING)
                .action(ctx -> Platform.runLater(() -> {
                    try {
                        logic.execute(ListCommand.COMMAND_WORD + " "
                                + combineArguments(ctx.arguments()));
                        ObservableList<ReadOnlyParcel> parcels = model.getUncompletedParcelList();
                        sender.send(parseDisplayParcels(formatParcelsForBot(parcels)),
                                ctx.chatId());
                    } catch (CommandException | ParseException e) {
                        sender.send("Sorry, I don't understand.", ctx.chatId());
                    }
                }))
                .build();
    }

    /**
     * Replicates the effects of DeleteCommand on ArkBot.
     */
    public Ability deleteCommand() {
        return Ability
                .builder()
                .name(DeleteCommand.COMMAND_WORD)
                .info("deletes parcel at selected index")
                .input(0)
                .locality(Locality.ALL)
                .privacy(PRIVACY_SETTING)
                .action((MessageContext ctx) -> Platform.runLater(() -> {
                    try {
                        logic.execute(DeleteCommand.COMMAND_WORD + " "
                                + combineArguments(ctx.arguments()));
                        ObservableList<ReadOnlyParcel> parcels = model.getUncompletedParcelList();
                        sender.send(parseDisplayParcels(formatParcelsForBot(parcels)), ctx.chatId());
                    } catch (CommandException | ParseException e) {
                        sender.send(BOT_MESSAGE_FAILURE, ctx.chatId());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }))
                .build();
    }

    /**
     * Replicates the effects of RedoCommand on ArkBot.
     */
    public Ability redoCommand() {
        return Ability
                .builder()
                .name(RedoCommand.COMMAND_WORD)
                .info("deletes parcel at selected index")
                .input(0)
                .locality(Locality.ALL)
                .privacy(PRIVACY_SETTING)
                .action((MessageContext ctx) -> Platform.runLater(() -> {
                    try {
                        logic.execute(RedoCommand.COMMAND_WORD);
                        sender.send(String.format(BOT_MESSAGE_SUCCESS, RedoCommand.COMMAND_WORD), ctx.chatId());
                    } catch (CommandException | ParseException e) {
                        sender.send(BOT_MESSAGE_FAILURE, ctx.chatId());
                    }
                }))
                .build();
    }

    /**
     * Replicates the effects of UndoCommand on ArkBot.
     */
    public Ability undoCommand() {
        return Ability
                .builder()
                .name(UndoCommand.COMMAND_WORD)
                .info("deletes parcel at selected index")
                .input(0)
                .locality(Locality.ALL)
                .privacy(PRIVACY_SETTING)
                .action((MessageContext ctx) -> Platform.runLater(() -> {
                    try {
                        logic.execute(UndoCommand.COMMAND_WORD);
                        sender.send(String.format(BOT_MESSAGE_SUCCESS, UndoCommand.COMMAND_WORD), ctx.chatId());
                    } catch (CommandException | ParseException e) {
                        sender.send(BOT_MESSAGE_FAILURE, ctx.chatId());
                    }
                }))
                .build();
    }

    /**
     * Replicates the effects of FindCommand on ArkBot.
     */
    public Ability findCommand() {
        return Ability
                .builder()
                .name(FindCommand.COMMAND_WORD)
                .info("adds parcel to list")
                .input(0)
                .locality(Locality.ALL)
                .privacy(PRIVACY_SETTING)
                .action(ctx -> Platform.runLater(() -> {
                    try {
                        logic.execute(FindCommand.COMMAND_WORD + " "
                                + combineArguments(ctx.arguments()));
                        ObservableList<ReadOnlyParcel> parcels = model.getUncompletedParcelList();
                        sender.send(parseDisplayParcels(formatParcelsForBot(parcels)), ctx.chatId());
                    } catch (CommandException | ParseException e) {
                        sender.send(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE),
                                ctx.chatId());
                    }
                }))
                .build();
    }

    /**
     * Command to complete tasks with QR code or number
     */
    public Ability completeCommand() {
        return Ability
                .builder()
                .name("complete")
                .info("completes a parcel in list")
                .input(0)
                .locality(Locality.ALL)
                .privacy(PRIVACY_SETTING)
                .action(ctx -> Platform.runLater(() -> {
                    String input = combineArguments(ctx.arguments());
                    try {
                        if (input.trim().equals("")) {
                            this.waitingForImage = true;
                            sender.send(BOT_MESSAGE_COMPLETE_COMMAND, ctx.chatId());
                        } else if (containsAllNumbers(input.trim())) {
                            logic.execute(EditCommand.COMMAND_WORD + " "
                                    + input + BOT_SET_COMPLETED);
                            ObservableList<ReadOnlyParcel> parcels = model.getUncompletedParcelList();
                            sender.send(parseDisplayParcels(formatParcelsForBot(parcels)), ctx.chatId());
                        } else {
                            sender.send(BOT_MESSAGE_FAILURE, ctx.chatId());
                        }
                    } catch (CommandException | ParseException e) {
                        sender.send(BOT_MESSAGE_FAILURE, ctx.chatId());
                    }
                }))
                .build();
    }

    /**
     * Command to cancel waiting for a QR to mark as completed.
     */
    public Ability cancelCommand() {
        return Ability
                .builder()
                .name("cancel")
                .info("cancels QR code upload")
                .input(0)
                .locality(Locality.ALL)
                .privacy(PRIVACY_SETTING)
                .action(ctx -> Platform.runLater(() -> {
                    this.waitingForImage = false;
                    sender.send(BOT_MESSAGE_CANCEL_COMMAND, ctx.chatId());
                }))
                .build();
    }

    /**
     * Command to advise user on usage of bot.
     */
    public Ability helpCommand() {
        return Ability
                .builder()
                .name("help")
                .info("adds parcel to list")
                .input(0)
                .locality(Locality.ALL)
                .privacy(PRIVACY_SETTING)
                .action(ctx -> Platform.runLater(() -> {
                    try {
                        sender.sendMessage(new SendMessage().setText(BOT_MESSAGE_HELP)
                                                                .setChatId(ctx.chatId())
                                                                .enableMarkdown(true));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }))
                .build();
    }

    /**
     * Takes in the array of arguments that is parsed into ArkBot and
     * returns a formatted string.
     */
    private String combineArguments(String[] arguments) {
        String result = "";

        for (int i = 0; i < arguments.length; i++) {
            result += arguments[i] + " ";
        }

        return result;
    }

    /**
     * Filters the list of parcels to only show Name, Address and Phone number
     * attributed to each parcel.
     */
    public ArrayList<DisplayParcel> formatParcelsForBot(ObservableList<ReadOnlyParcel> parcels) {
        ArrayList<DisplayParcel> toDisplay = new ArrayList<>();

        Iterator<ReadOnlyParcel> parcelIterator = parcels.iterator();

        while (parcelIterator.hasNext()) {
            ReadOnlyParcel currParcel = parcelIterator.next();
            DisplayParcel displayed = new DisplayParcel(currParcel.getName(), currParcel.getAddress(),
                    currParcel.getPhone());
            toDisplay.add(displayed);
        }

        return toDisplay;
    }

    /**
     * Formats a list of Parcels to be displayed on ArkBot
     */
    public String parseDisplayParcels(ArrayList<DisplayParcel> displayParcels) {
        if (displayParcels.size() == 0) {
            return "No parcels to be displayed.";
        } else {
            String result = "";
            for (int i = 0; i < displayParcels.size(); i++) {
                result += (i + 1) + ". " + displayParcels.get(i).toString() + "\n";
            }

            return result;
        }
    }

    @Override
    public int creatorId() {
        return 0;
    }

    @Override
    public boolean checkGlobalFlags(Update update) {
        return true;
    }

    /**
     * This ability has an extra "flag". It needs a photo to activate.
     */
    public Ability onPhotoCommand() {
        return Ability
                .builder()
                .name(DEFAULT)
                .flag(PHOTO)
                .info("receives Photos")
                .input(0)
                .locality(Locality.ALL)
                .privacy(PRIVACY_SETTING)
                .action((MessageContext ctx) -> Platform.runLater(() -> {
                    Update update = ctx.update();
                    if (update.hasMessage() && update.getMessage().hasPhoto()) {
                        java.io.File picture = getPictureFileFromUpdate(update);
                        try {
                            ReadOnlyParcel retrievedParcel = retrieveParcelFromPictureFile(picture);
                            logger.info("The retrieved parcel is: " + retrievedParcel);
                            if (retrievedParcel.equals(null)) {
                                sender.send("Sorry, I didn't seem to understand your image. Please try again.",
                                        ctx.chatId());
                            } else if (this.waitingForImage) {
                                int indexZeroBased = model.getUncompletedParcelList().indexOf(retrievedParcel);
                                if (indexZeroBased < 0) {
                                    sender.send("The parcel cannot be found! Please try again.",
                                            ctx.chatId());
                                } else {
                                    performCompleteParcel(sender, logic, indexZeroBased + 1,
                                            retrievedParcel, ctx.chatId());
                                }
                            } else {
                                sender.send("Here are the details of the parcel: \n"
                                                + retrievedParcel.toString(), ctx.chatId());
                            }
                        } catch (ParseException | CommandException | QRreadException e) {
                            sender.send(BOT_MESSAGE_FAILURE, ctx.chatId());
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                })).build();
    }

    /**
     * Abstracted method that performs edits a parcel to completed.
     */
    private void performCompleteParcel(MessageSender sender, Logic logic, int indexOfParcel,
                                       ReadOnlyParcel retrievedParcel, Long chatId)
                                       throws CommandException, ParseException, TelegramApiException {
        logic.execute(EditCommand.COMMAND_WORD + " "
                + indexOfParcel + BOT_SET_COMPLETED);
        sender.send("Here are the details of the parcel you just completed: \n"
                    + retrievedParcel.toString(), chatId);

    }

    /**
     * Method to extract ReadOnlyParcel from picture file using zxing qr code analyser.
     */
    private ReadOnlyParcel retrieveParcelFromPictureFile(java.io.File picture) throws ParseException, QRreadException {
        QRcodeAnalyser qrca = new QRcodeAnalyser(picture);
        ParcelParser pp = new ParcelParser();
        logger.info("The decoded text is: " + qrca.getDecodedText());
        return pp.parse(qrca.getDecodedText());
    }

    /**
     * Method to extract picture file from update.
     */
    private java.io.File getPictureFileFromUpdate(Update update) {
        return downloadPhotoByFilePath(getFilePath(getPhoto(update)));
    }
    //@@author

    /* The following three methods are from https://github.com/rubenlagus/TelegramBots/wiki/FAQ#how_to_get_picture */

    /**
     * Retrieving photo from update
     */
    private PhotoSize getPhoto(Update update) {
        // Check that the update contains a message and the message has a photo
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            // When receiving a photo, you usually get different sizes of it
            List<PhotoSize> photos = update.getMessage().getPhoto();

            // We fetch the bigger photo
            return photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null);
        }

        // Return null if not found
        return null;
    }

    /**
     * Retrieving filepath from photo
     */
    private String getFilePath(PhotoSize photo) {
        Objects.requireNonNull(photo);

        if (photo.hasFilePath()) { // If the file_path is already present, we are done!
            return photo.getFilePath();
        } else { // If not, let find it
            // We create a GetFile method and set the file_id from the photo
            GetFile getFileMethod = new GetFile();
            getFileMethod.setFileId(photo.getFileId());
            try {
                // We execute the method using AbsSender::getFile method.
                File file = getFile(getFileMethod);
                // We now have the file_path
                return file.getFilePath();
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        return null; // Just in case
    }

    /**
     * Downloading the photo from telegram.
     */
    private java.io.File downloadPhotoByFilePath(String filePath) {
        try {
            // Download the file calling AbsSender::downloadFile method
            return downloadFile(filePath);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return null;
    }

    //@@author fustilio
    /**
     * Returns true if a given string contains all numbers.
     */
    public static boolean containsAllNumbers(String test) {
        String regex = "\\d+";
        return test.matches(regex);
    }

    @VisibleForTesting
    void setSender(MessageSender sender) {
        this.sender = sender;
    }

    @VisibleForTesting
    boolean getWaitingForImageFlag() {
        return this.waitingForImage;
    }
}
//@@author
