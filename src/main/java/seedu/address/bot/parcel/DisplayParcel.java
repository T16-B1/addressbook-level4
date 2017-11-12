package seedu.address.bot.parcel;

import seedu.address.model.parcel.Address;
import seedu.address.model.parcel.Name;
import seedu.address.model.parcel.Phone;

/**
 * Created by Francis on 31/10/2017.
 */
public class DisplayParcel {

    private Name name;
    private Address address;
    private Phone phone;

    public DisplayParcel(Name name, Address address, Phone phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Name: " + this.name.toString()
                + "\nAddress: " + this.address.toString()
                + "\nPhone: " + this.phone.toString();
    }
}
