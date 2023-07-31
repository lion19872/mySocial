data class Post(
    val id: Int,
    val date: Int,
    val text: String,
    val comment: Comment,
    val copyright: Copyright,
    val likes: Likes,
    val reposts: Reposts,
    val views: Views
)

data class Comment(
    val id: Int,
    val from_id: Int,
    val date: Int,
    val text: String,
    val reply_to_user: Int = 0,
    val reply_to_comment: Int = 0
)

data class Copyright(
    val id: Int,
    val link: String?,
    val name: String?,
    val type: String?
)

data class Likes(
    val count: Int,
    val userLikes: Int,
    val canLike: Int,
    val canPublish: Int
)

data class Reposts(
    val count: Int,
    val userReposted: Int
)

data class Views(
    val count: Int
)

class WallService {
    private var posts = emptyArray<Post>()
    private var comments = emptyArray<Comment>()

    private fun generateNewCommentId(): Int {
        return comments.maxByOrNull { it.id }?.id?.plus(1) ?: 1
    }

    fun addPost(post: Post): Post {
        val newPost = post.copy(id = if (posts.isEmpty()) 1 else posts.maxByOrNull { it.id }!!.id + 1)
        posts = posts.plus(newPost)
        return newPost
    }

    fun updatePost(post: Post): Boolean {
        val existingPost = posts.find { it.id == post.id }
        return if (existingPost != null) {
            val updatedPost = existingPost.copy(
                text = post.text,
                comment = post.comment,
                copyright = post.copyright,
                likes = post.likes,
                reposts = post.reposts,
                views = post.views
            )
            val index = posts.indexOf(existingPost)
            posts[index] = updatedPost
            true
        } else {
            false
        }
    }

    fun removePostById(postId: Int): Boolean {
        val existingPost = posts.find { it.id == postId }
        return if (existingPost != null) {
            posts = posts.filterNot { it.id == postId }.toTypedArray()
            true
        } else {
            false
        }
    }

    fun getPostById(postId: Int): Post? {
        return posts.find { it.id == postId }
    }

    fun getAllPosts(): List<Post> {
        return posts.toList()
    }

    fun getPostsCount(): Int {
        return posts.size
    }
    fun createComment(postId: Int, comment: Comment): Comment {
        val post = posts.find { it.id == postId }
        if (post != null) {
            val newComment = comment.copy(id = generateNewCommentId(), from_id = comment.from_id, date = comment.date)
            comments = comments.plus(newComment)
            return newComment
        } else {
            throw PostNotFoundException("Пост с ID $postId не найден.")
        }
    }
}

class PostNotFoundException(message: String) : Exception(message)

fun main() {
    val wallService = WallService()

    val post1 = Post(
        id = 1,
        date = 1672406400,
        text = "Привет, мир!",
        comment = Comment(id = 1,
            from_id = 123,
            date = 1672406401,
            text = "Комментарий к посту 1",
            reply_to_user = 0,
            reply_to_comment = 0),
        copyright = Copyright(id = 123, link = null, name = "Dmitry Levinski", type = "Author"),
        likes = Likes(count = 10, userLikes = 1, canLike = 0, canPublish = 1),
        reposts = Reposts(count = 2, userReposted = 0),
        views = Views(count = 100)
    )

    val post2 = Post(
        id = 2,
        date = 1672492800,
        text = "Какой замечательный день!",
        comment = Comment( id = 2,
            from_id = 456,
            date = 1672492801,
            text = "Комментарий к посту 2",
            reply_to_user = 0,
            reply_to_comment = 0),
        copyright = Copyright(id = 456, link = null, name = "Dmitry Levinski", type = "Author"),
        likes = Likes(count = 15, userLikes = 0, canLike = 1, canPublish = 1),
        reposts = Reposts(count = 1, userReposted = 1),
        views = Views(count = 50)
    )

    wallService.addPost(post1)
    wallService.addPost(post2)

    val allPosts = wallService.getAllPosts()
    println("Список всех постов:")
    for (post in allPosts) {
        println("ID: ${post.id}, Текст: ${post.text}, Лайки: ${post.likes.count}, Комментарии: ${post.comment.text}")
    }

    val postIdToUpdate = 1
    val postToUpdate = wallService.getPostById(postIdToUpdate)
    if (postToUpdate != null) {
        val updatedPost = postToUpdate.copy(text = "Обновленный текст поста")
        wallService.updatePost(updatedPost)
        println("Пост с ID $postIdToUpdate обновлен.")
    } else {
        println("Пост с ID $postIdToUpdate не найден.")
    }

    val postIdToRemove = 2
    val isRemoved = wallService.removePostById(postIdToRemove)
    if (isRemoved) {
        println("Пост с ID $postIdToRemove удален.")
    } else {
        println("Пост с ID $postIdToRemove не найден.")
    }

    val postsCount = wallService.getPostsCount()
    println("Общее количество постов: $postsCount")

    // Adding comments
    val comment1 = Comment(id = 1, from_id = 123, date = 1672406401, text = "Комментарий к посту 1")
    wallService.createComment(1, comment1)

    val comment2 = Comment(id = 2, from_id = 456, date = 1672492801, text = "Комментарий к посту 2")
    wallService.createComment(2, comment2)
}
