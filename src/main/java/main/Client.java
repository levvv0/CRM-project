package main;
public class Client {

    private int id;
    private String name;
    private String phone;
    private String email;

    public Client(ClientBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.phone = builder.phone;
        this.email = builder.email;
    }

    public static class ClientBuilder{

        private int id;
        private String name;
        private String phone;
        private String email;

        public ClientBuilder SId(int id){
            this.id = id;
            return this;
        }

        public ClientBuilder SName(String name){
            this.name = name;
            return this;
        }

        public ClientBuilder SPhone(String phone){
            this.phone = phone;
            return this;
        }

        public ClientBuilder SEmail(String email){
            this.email = email;
            return this;
        }

        public Client build(){
            return new Client(this);
        }
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getPhone(){
        return phone;
    }

    public String getEmail(){
        return email;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public void setEmail(String email){
        this.email = email;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
