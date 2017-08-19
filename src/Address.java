import java.lang.String;
import java.lang.StringBuffer;

public class Address
{

    String firstname,lastname,street,city,state,zip;
    public void Address()
    {
        firstname = new String();
        lastname = new String();
        street = new String();
        city = new String();
        state = new String();
        zip = new String();
    }

    public void setFirstname(String v)
    {
        firstname = v;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public void setLastname(String v)
    {
        lastname = v;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setStreet(String v)
    {
        street = v;
    }

    public String getStreet()
    {
        return street;
    }

    public void setState(String v)
    {
        state = v;
    }

    public String getState()
    {
        return state;
    }

    public void setCity(String v)
    {
        city = v;
    }

    public String getCity()
    {
        return city;
    }

    public void setZip(String v)
    {
        zip = v;
    }

    public String getZip()
    {
        return zip;
    }

    public String toString()
    {
        String empty = "";
        if ((firstname == null) || empty.equals(firstname)) {
            firstname = "<em>(no firstname specified)</em>";
        }
        if ((lastname == null) || empty.equals(lastname)) {
            lastname = "<em>(no lastname specified)</em>";
        }
        if ((street == null) || empty.equals(street)) {
            street = "<em>(no street specified)</em>";
        }
        if ((city == null) || empty.equals(city)) {
            city = "<em>(no city specified)</em>";
        }
        if ((state == null) || empty.equals(state)) {
            state = "<em>(no state specified)</em>";
        } else {
            int abbrevIndex = state.indexOf('(') + 1;
            state = state.substring(abbrevIndex,
                                    abbrevIndex + 2);
        }
        if ((zip == null) || empty.equals(zip)) {
            zip = "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<html><p align=center>");
        sb.append(firstname);
        sb.append("&nbsp;");
        sb.append(lastname);
        sb.append("<br>");
        sb.append(street);
        sb.append("<br>");
        sb.append(city);
        sb.append(" ");
        sb.append(state); //should format
        sb.append(" ");
        sb.append(zip);
        sb.append("</p></html>");
        return sb.toString();

    }
}
