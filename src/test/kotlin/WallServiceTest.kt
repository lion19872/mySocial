
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class WallServiceTest {
    @Test
    fun testAddPost() {
        val wallService = WallService()
        val post = Post(
            id = 1,
            date = 1672406400,
            text = "Test post",
            comment = Comment(id = 1,
                from_id = 123,
                date = 1672406401,
                text = "Комментарий к посту 1",
                reply_to_user = 0,
                reply_to_comment = 0),
            copyright = Copyright(id = 123, link = null, name = "Test Company", type = "Company"),
            likes = Likes(count = 0, userLikes = 0, canLike = 1, canPublish = 1),
            reposts = Reposts(count = 0, userReposted = 0),
            views = Views(count = 0)
        )

        val addedPost = wallService.addPost(post)

        assertEquals(post, addedPost)
        assertEquals(1, wallService.getPostsCount())
    }

    @Test
    fun testUpdatePost() {
        val wallService = WallService()
        val post = Post(
            id = 1,
            date = 1672406400,
            text = "Test post",
            comment = Comment(id = 1,
                from_id = 123,
                date = 1672406401,
                text = "Комментарий к посту 1",
                reply_to_user = 0,
                reply_to_comment = 0),
            copyright = Copyright(id = 123, link = null, name = "Test Company", type = "Company"),
            likes = Likes(count = 0, userLikes = 0, canLike = 1, canPublish = 1),
            reposts = Reposts(count = 0, userReposted = 0),
            views = Views(count = 0)
        )

        wallService.addPost(post)

        val updatedPost = post.copy(text = "Updated test post")
        val isUpdated = wallService.updatePost(updatedPost)

        assertTrue(isUpdated)
        assertEquals(updatedPost, wallService.getPostById(1))
    }

    @Test
    fun testRemovePost() {
        val wallService = WallService()
        val post = Post(
            id = 1,
            date = 1672406400,
            text = "Test post",
            comment = Comment(id = 1,
                from_id = 123,
                date = 1672406401,
                text = "Комментарий к посту 1",
                reply_to_user = 0,
                reply_to_comment = 0),
            copyright = Copyright(id = 123, link = null, name = "Test Company", type = "Company"),
            likes = Likes(count = 0, userLikes = 0, canLike = 1, canPublish = 1),
            reposts = Reposts(count = 0, userReposted = 0),
            views = Views(count = 0)
        )

        wallService.addPost(post)

        val isRemoved = wallService.removePostById(1)

        assertTrue(isRemoved)
        assertNull(wallService.getPostById(1))
        assertEquals(0, wallService.getPostsCount())
    }

    @Test
    fun testUnsuccessfulUpdatePost() {
        val wallService = WallService()
        val post = Post(
            id = 1,
            date = 1672406400,
            text = "Test post",
            comment = Comment(id = 1,
                from_id = 123,
                date = 1672406401,
                text = "Комментарий к посту 1",
                reply_to_user = 0,
                reply_to_comment = 0),
            copyright = Copyright(id = 123, link = null, name = "Test Company", type = "Company"),
            likes = Likes(count = 0, userLikes = 0, canLike = 1, canPublish = 1),
            reposts = Reposts(count = 0, userReposted = 0),
            views = Views(count = 0)
        )

        wallService.addPost(post)

        val nonExistingPost = Post(
            id = 2,
            date = 1672406400,
            text = "Non-existing post",
            comment = Comment(id = 1,
                from_id = 123,
                date = 1672406401,
                text = "Комментарий к посту 1",
                reply_to_user = 0,
                reply_to_comment = 0),
            copyright = Copyright(id = 123, link = null, name = "Test Company", type = "Company"),
            likes = Likes(count = 0, userLikes = 0, canLike = 1, canPublish = 1),
            reposts = Reposts(count = 0, userReposted = 0),
            views = Views(count = 0)
        )

        val isUpdated = wallService.updatePost(nonExistingPost)

        assertFalse(isUpdated)
    }
    @Test
    fun testGetPostById_ThrowsPostNotFoundException() {
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

        wallService.addPost(post1)

        // Обращаемся к посту с несуществующим ID
        val nonExistentPostId = 999
        val exception = assertThrows<PostNotFoundException> {
            wallService.getPostById(nonExistentPostId)
        }

        assertEquals("Пост с ID $nonExistentPostId не найден.", exception.message)
    }
}
