package platinum.whatstheplanserver.models;

public class Guest {

    private String guest_email;
    private String guest_name;
    private String guest_id;

    public Guest() {
    }

    public Guest(String guest_email, String guest_name, String guest_id) {
        this.guest_email = guest_email;
        this.guest_name = guest_name;
        this.guest_id = guest_id;
    }

    public String getGuest_email() {
        return guest_email;
    }

    public void setGuest_email(String guest_email) {
        this.guest_email = guest_email;
    }

    public String getGuest_name() {
        return guest_name;
    }

    public void setGuest_name(String guest_name) {
        this.guest_name = guest_name;
    }

    public String getGuest_id() {
        return guest_id;
    }

    public void setGuest_id(String guest_id) {
        this.guest_id = guest_id;
    }
}
