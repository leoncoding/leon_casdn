package suzhou.dataup.cn.myapplication.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/24.
 */
public class BlogTagBean implements Serializable {
    private int id;
    private String name;
    private String alias;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "BlogTagBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}
