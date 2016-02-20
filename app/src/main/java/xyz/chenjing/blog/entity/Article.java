package xyz.chenjing.blog.entity;

import com.londonx.lutil.entity.LEntity;

import java.util.List;

/**
 * Created by london on 16/2/19.
 * Article(blog)
 */
public class Article extends LEntity {

    /**
     * id : 1
     * title : Hello World
     * description : Hello World,个人博客上线啦
     * result_type : collect
     * cover : /image/no_pic.jpg
     * items : [{"content":"Hello World  \r\n","file_url":null},{"content":"经过了一段时间的编写，自己也真的拥有了一个真正意义的博客，全手工制作，拒绝模板","file_url":null},{"content":"本博客收集的内容目前分为三大块 ： 收集的软件、自己创作的一些应用、及生活学习中的日志。","file_url":null},{"content":"当然，博客目前还不是分完善，一些想法什么的还需要再次改进，如果你在我的博客你看到些什么有用的内容，那么我将会很高兴。谢谢","file_url":null},{"content":"如果你有好的建议什么的也可以在最右下方给我留言","file_url":null}]
     */
    public String title;
    public String description;
    public Type result_type;
    public String cover;
    public List<Item> items;

    public enum Type {
        all, work, collect, diary
    }
}
