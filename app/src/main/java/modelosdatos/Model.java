package modelosdatos;
public class Model {
    private  String id;
    private String group;
    private String lecture;
    private String activity;

    //construye objeto vacio
    public Model() {
    }
    // con id para consultas
    public Model(String id, String group, String lecture, String activity) {
        this.id = id;
        this.group = group;
        this.lecture = lecture;
        this.activity = activity;
    }
    //cuando sin guardamos dato
    public Model(String group, String lecture, String activity) {
        this.group = group;
        this.lecture = lecture;
        this.activity = activity;
    }

//para obtener las cosas
    public String getId() {
        return id;
    }

    public String getGroup() {
        return group;
    }

    public String getLecture() {
        return lecture;
    }

    public String getActivity() {
        return activity;
    }


    //para poner cosas son los sets
    public void setId(String id) {
        this.id = id;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
