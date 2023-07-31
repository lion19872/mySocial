import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class WallServiceTest {
    @Test
    fun testAddPost() {
        val wallService = WallService()
        val post = Post(
            id = 1,
            date = 1672406400,
            text = "Test post",
            comments = Comments(count = 0, can_post = 1, groups_can_post = true, can_close = false, can_open = true),
            copyright = Copyright(id = 123, link = null, name = "Test Company", type = "Company"),
            likes = Likes(count = 0, user_likes = 0, can_like = 1, can_publish = 1),
            reposts = Reposts(count = 0, user_reposted = 0),
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
            comments = Comments(count = 0, can_post = 1, groups_can_post = true, can_close = false, can_open = true),
            copyright = Copyright(id = 123, link = null, name = "Test Company", type = "Company"),
            likes = Likes(count = 0, user_likes = 0, can_like = 1, can_publish = 1),
            reposts = Reposts(count = 0, user_reposted = 0),
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
            comments = Comments(count = 0, can_post = 1, groups_can_post = true, can_close = false, can_open = true),
            copyright = Copyright(id = 123, link = null, name = "Test Company", type = "Company"),
            likes = Likes(count = 0, user_likes = 0, can_like = 1, can_publish = 1),
            reposts = Reposts(count = 0, user_reposted = 0),
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
            comments = Comments(count = 0, can_post = 1, groups_can_post = true, can_close = false, can_open = true),
            copyright = Copyright(id = 123, link = null, name = "Test Company", type = "Company"),
            likes = Likes(count = 0, user_likes = 0, can_like = 1, can_publish = 1),
            reposts = Reposts(count = 0, user_reposted = 0),
            views = Views(count = 0)
        )

        wallService.addPost(post)

        val nonExistingPost = Post(
            id = 2,
            date = 1672406400,
            text = "Non-existing post",
            comments = Comments(count = 0, can_post = 1, groups_can_post = true, can_close = false, can_open = true),
            copyright = Copyright(id = 123, link = null, name = "Test Company", type = "Company"),
            likes = Likes(count = 0, user_likes = 0, can_like = 1, can_publish = 1),
            reposts = Reposts(count = 0, user_reposted = 0),
            views = Views(count = 0)
        )

        val isUpdated = wallService.updatePost(nonExistingPost)

        assertFalse(isUpdated)
    }

}
