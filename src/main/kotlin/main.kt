data class Post(
    val id: Int,
    val date: Long,
    val text: String?,
    var comments: Comments?,
    val copyright: Copyright?,
    val likes: Likes?,
    val reposts: Reposts?,
    val views: Views?,
    val ownerId: Int = 0,
    val fromId: Int? = null,
    val createdBy: Int = 0,
    val replyOwnerId: Int? = null,
    val replyPostId: Int? = null,
    val friendsOnly: Boolean? = null,
    val postType: String? = null,
    val postSource: PostSource? = null,
    val attachments: List<Attachment>? = null,
    val geo: Geo? = null,
    val signerId: Int? = null,
    val copyHistory: List<Post>? = null,
    val canPin: Boolean = false,
    val canDelete: Boolean = false,
    val canEdit: Boolean? = null,
    val isPinned: Boolean = false,
    val markedAsAds: Boolean? = null,
    val isFavorite: Boolean = false
) {
    override fun toString(): String {
        return """
            |ID: $id
            |Date: $date
            |Text: $text
            |Comments: $comments
            |Copyright: $copyright
            |Likes: $likes
            |Reposts: $reposts
            |Views: $views
            |OwnerId: $ownerId
            |FromId: $fromId
            |CreatedBy: $createdBy
            |ReplyOwnerId: $replyOwnerId
            |ReplyPostId: $replyPostId
            |FriendsOnly: $friendsOnly
            |PostType: $postType
            |PostSource: $postSource
            |Attachments: $attachments
            |Geo: $geo
            |SignerId: $signerId
            |CopyHistory: $copyHistory
            |CanPin: $canPin
            |CanDelete: $canDelete
            |CanEdit: $canEdit
            |IsPinned: $isPinned
            |MarkedAsAds: $markedAsAds
            |IsFavorite: $isFavorite
        """.trimMargin()
    }
}

data class Comments(
    val count: Int?, val can_post: Int?, val groups_can_post: Boolean?, val can_close: Boolean?, val can_open: Boolean?
)

data class Comment(
    val id: Int, val postId: Int, val text: String, val date: Long
)

data class Copyright(
    val id: Int?, val link: String?, val name: String?, val type: String?
)

data class Likes(
    val count: Int?, val user_likes: Int?, val can_like: Int?, val can_publish: Int?
)

data class Reposts(
    val count: Int?, val user_reposted: Int?
)

data class Views(
    val count: Int?
)

data class PostSource(
    val type: String?
)

data class Geo(
    val coordinates: String?, val place: Place?
)

data class Place(
    val title: String?,
    val latitude: Double?,
    val longitude: Double?,
    val created: Int?,
    val icon: String?,
    val checkins: Int?,
    val updated: Int?,
    val type: Int?,
    val country: Int?,
    val city: Int?,
    val address: String?
)

abstract class Attachment(val type: String)

data class PhotoAttachment(val photo: Photo) : Attachment("photo")

data class Photo(
    val id: Int, val ownerId: Int, val photo130: String, val photo604: String
)

class WallService {
    private val posts = mutableListOf<Post>()
    private var commentIdCounter = 0

    fun createComment(postId: Int, commentText: String): Comment {
        val post = posts.find { it.id == postId } ?: throw PostNotFoundException("Post with ID $postId not found")

        val newCommentId = ++commentIdCounter
        val newComment = Comment(newCommentId, postId, commentText, System.currentTimeMillis())

        // Add the new comment to the list of comments for the post
        val updatedComments = post.comments?.copy(count = (post.comments?.count ?: 0) + 1)
        post.comments = updatedComments

        return newComment
    }

    fun addPost(post: Post): Post {
        val newPost = post.copy(id = if (posts.isEmpty()) 1 else (posts.maxByOrNull { it.id }?.id ?: 0) + 1)
        posts.add(newPost)
        return newPost
    }

    fun updatePost(post: Post): Boolean {
        val existingPost = posts.find { it.id == post.id }
        return if (existingPost != null) {
            val existingComments = existingPost.comments

            val updatedPost = existingPost.copy(
                text = post.text,
                comments = post.comments,
                copyright = post.copyright,
                likes = post.likes,
                reposts = post.reposts,
                views = post.views
            )

            // Restore the original comments value
            updatedPost.comments = existingComments

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
    val wallService = WallService()

    val post1 = Post(
        id = 1,
        date = 1672406400L,
        text = "Сегодня я опубликовал новую статью на своем сайте. Приглашаю всех прочитать! #новости #статья",
        comments = Comments(count = 5, can_post = 1, groups_can_post = true, can_close = false, can_open = true),
        copyright = Copyright(id = 123, link = null, name = "Dmitry Levinski", type = "Author"),
        likes = Likes(count = 10, user_likes = 1, can_like = 0, can_publish = 1),
        reposts = Reposts(count = 2, user_reposted = 0),
        views = Views(count = 100),
    )

    val post2 = Post(
        id = 2,
        date = 1672492800L,
        text = "Вчера вернулся из Таиланда. Путешествие было замечательным! Поделился некоторыми фотографиями в своем блоге. #путешествия #Таиланд",
        comments = Comments(count = 3, can_post = 1, groups_can_post = true, can_close = false, can_open = true),
        copyright = Copyright(id = 456, link = null, name = "Dmitry Levinski", type = "Author"),
        likes = Likes(count = 15, user_likes = 0, can_like = 1, can_publish = 1),
        reposts = Reposts(count = 1, user_reposted = 1),
        views = Views(count = 50)
    )

    val post3 = Post(
        id = 3,
        date = 1672585600L,
        text = "Сегодня посетил занятие по программированию. Обсуждали интересные алгоритмы и практиковались в написании кода на Kotlin. Было продуктивно! #программирование #обучение",
        comments = Comments(count = 7, can_post = 1, groups_can_post = true, can_close = false, can_open = true),
        copyright = Copyright(id = 789, link = null, name = "Dmitry Levinski", type = "Author"),
        likes = Likes(count = 8, user_likes = 1, can_like = 0, can_publish = 1),
        reposts = Reposts(count = 0, user_reposted = 0),
        views = Views(count = 70)
    )

    val post4 = Post(
        id = 4,
        date = 1672672000L,
        text = "Сегодня посетил музей современного искусства. Впечатления зашкаливают! #искусство #музей",
        comments = Comments(count = 2, can_post = 1, groups_can_post = true, can_close = false, can_open = true),
        copyright = Copyright(id = 101, link = null, name = "Dmitry Levinski", type = "Author"),
        likes = Likes(count = 12, user_likes = 1, can_like = 0, can_publish = 1),
        reposts = Reposts(count = 3, user_reposted = 0),
        views = Views(count = 60)
    )

    wallService.addPost(post1)
    wallService.addPost(post2)
    wallService.addPost(post3)
    wallService.addPost(post4)


    val allPosts = wallService.getAllPosts()
    println("Список всех постов:")
    for (post in allPosts) {
        println("ID: ${post.id}, Текст: ${post.text}, Лайки: ${post.likes?.count}, Комментарии: ${post.comments?.count}")
    }

    val postIdToUpdate = 1
    val postToUpdate = wallService.getPostById(postIdToUpdate)
    if (postToUpdate != null) {
        val updatedPost = postToUpdate.copy(text = "Обновленный текст поста")
        wallService.updatePost(updatedPost)
        println("Пост $postIdToUpdate обновлен.")
    } else {
        println("Пост $postIdToUpdate не найден.")
    }

    val postIdToRemove = 2
    val isRemoved = wallService.removePostById(postIdToRemove)
    if (isRemoved) {
        println("Пост $postIdToRemove удален.")
    } else {
        println("Пост $postIdToRemove не найден.")
    }

    val postsCount = wallService.getPostsCount()
    println("Общее количество постов: $postsCount")

}

class PostNotFoundException(message: String) : Exception(message)