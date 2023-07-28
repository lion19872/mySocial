data class Post(
    val id: Int,
    val date: Int,
    val text: String,
    val comments: Comments,
    val copyright: Copyright,
    val likes: Likes,
    val reposts: Reposts,
    val views: Views
)

data class Comments(
    val count: Int,
    val canPost: Int,
    val groupsCanPost: Boolean,
    val canClose: Boolean,
    val canOpen: Boolean
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
    private val posts = mutableListOf<Post>()

    fun addPost(post: Post): Post {
        val newPost = post.copy(id = if (posts.isEmpty()) 1 else posts.maxByOrNull { it.id }!!.id + 1)
        posts.add(newPost)
        return newPost
    }

    fun updatePost(post: Post): Boolean {
        val existingPost = posts.find { it.id == post.id }
        return if (existingPost != null) {
            val index = posts.indexOf(existingPost)
            posts[index] = post
            true
        } else {
            false
        }
    }

    fun removePostById(postId: Int): Boolean {
        val existingPost = posts.find { it.id == postId }
        return if (existingPost != null) {
            posts.remove(existingPost)
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
}

fun main() {
    // Создаем объект WallService
    val wallService = WallService()

    val post1 = Post(
        id = 1,
        date = 1672406400,
        text = "Привет, мир!",
        comments = Comments(count = 5, canPost = 1, groupsCanPost = true, canClose = false, canOpen = true),
        copyright = Copyright(id = 123, link = null, name = "OpenAI", type = "Company"),
        likes = Likes(count = 10, userLikes = 1, canLike = 0, canPublish = 1),
        reposts = Reposts(count = 2, userReposted = 0),
        views = Views(count = 100)
    )

    val post2 = Post(
        id = 2,
        date = 1672492800,
        text = "Какой замечательный день!",
        comments = Comments(count = 3, canPost = 1, groupsCanPost = true, canClose = false, canOpen = true),
        copyright = Copyright(id = 456, link = null, name = "ChatGPT", type = "AI Model"),
        likes = Likes(count = 15, userLikes = 0, canLike = 1, canPublish = 1),
        reposts = Reposts(count = 1, userReposted = 1),
        views = Views(count = 50)
    )

    // Добавляем посты в WallService
    wallService.addPost(post1)
    wallService.addPost(post2)

    // Получаем все посты и выводим их на экран
    val allPosts = wallService.getAllPosts()
    println("Список всех постов:")
    for (post in allPosts) {
        println("ID: ${post.id}, Текст: ${post.text}, Лайки: ${post.likes.count}, Комментарии: ${post.comments.count}")
    }

    // Получаем и обновляем пост по ID
    val postIdToUpdate = 1
    val postToUpdate = wallService.getPostById(postIdToUpdate)
    if (postToUpdate != null) {
        val updatedPost = postToUpdate.copy(text = "Обновленный текст поста")
        wallService.updatePost(updatedPost)
        println("Пост с ID $postIdToUpdate обновлен.")
    } else {
        println("Пост с ID $postIdToUpdate не найден.")
    }

    // Удаляем пост по ID
    val postIdToRemove = 2
    val isRemoved = wallService.removePostById(postIdToRemove)
    if (isRemoved) {
        println("Пост с ID $postIdToRemove удален.")
    } else {
        println("Пост с ID $postIdToRemove не найден.")
    }

    // Выводим общее количество постов
    val postsCount = wallService.getPostsCount()
    println("Общее количество постов: $postsCount")
}
