package app.wowdrobe.com.tags

data class Tag(
    val name: String,
    val image: String,
    val tips: List<String> = emptyList()
) {
    constructor() : this("", "", emptyList())

    fun mapWithoutTips() = TagWithoutTips(
        name = name,
        image = image
    )
}

data class TagWithoutTips(
    val name: String,
    val image: String,
) {
    constructor() : this("", "")

    fun mapWithTips() = Tag(
        name = name,
        image = image
    )
}

data class Groups(
    val name: String,
    val tags: List<Tag> = emptyList()
) {
    constructor() : this("", emptyList())

    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            name,
            "${name.first()} ${tags.joinToString(" ") { it.name }}",
            "${tags.any()}",
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}


val wasteGroups = listOf(
    Groups(
        name = "Topwear",
        tags = listOf(
            Tag(
                name = "T-Shirts",
                tips = listOf(
                    "Look for organic cotton or recycled polyester T-shirts for eco-friendly options.",
                    "Consider buying second-hand T-shirts to reduce textile waste.",
                    "Support brands that use sustainable manufacturing practices for their T-shirts.",
                    "Choose timeless designs and high-quality materials to ensure longevity.",
                    "Explore DIY options for upcycling old T-shirts into new fashion pieces."
                ),
                image = "https://firebasestorage.googleapis.com/v0/b/wowdrobe-e3d22.appspot.com/o/tshirts.png?alt=media&token=434842d2-3bca-4908-a1a8-29ed9fce63fc"
            ),
            Tag(
                name = "Tops",
                tips = listOf(
                    "Opt for natural fibers like organic cotton, linen, or hemp for sustainable tops.",
                    "Shop from thrift stores or clothing swaps to find unique and eco-friendly tops.",
                    "Support ethical fashion brands that prioritize fair labor and environmental responsibility.",
                    "Consider the versatility and durability of tops to minimize wardrobe turnover.",
                    "Explore local artisans or small businesses for handmade and sustainable top options."
                ),
                image = "https://firebasestorage.googleapis.com/v0/b/wowdrobe-e3d22.appspot.com/o/tops.png?alt=media&token=084b656e-f8be-4f8a-9681-ebf8cbcb3efb"

            ),
            Tag(
                name = "Shirts",
                tips = listOf(
                    "Look for shirts made from sustainable materials like Tencel, bamboo, or recycled fabrics.",
                    "Shop vintage or second-hand shirts to reduce the environmental impact of your wardrobe.",
                    "Support brands that offer repair services or promote garment longevity.",
                    "Consider the carbon footprint and ethical practices of brands when purchasing shirts.",
                    "Explore minimalist or capsule wardrobes to prioritize quality over quantity."
                ),
                image = "https://firebasestorage.googleapis.com/v0/b/wowdrobe-e3d22.appspot.com/o/shirts.png?alt=media&token=a4de8373-e8a1-4caa-94c1-9ac33ad0e031"
            ),
        )
    ),
    Groups(
        name = "Bottom Wear",
        tags = listOf(
            Tag(
                name = "Jeans",
                tips = listOf(
                    "Choose jeans made from organic cotton or recycled denim for sustainable options.",
                    "Shop for second-hand or vintage jeans to reduce textile waste.",
                    "Support denim brands that prioritize ethical production and labor practices.",
                    "Consider investing in timeless and durable jeans to minimize fashion waste.",
                    "Explore repair or alteration services to extend the lifespan of your jeans."
                ),
                image = "https://firebasestorage.googleapis.com/v0/b/wowdrobe-e3d22.appspot.com/o/jeans.png?alt=media&token=57b68f17-e856-4624-a696-e2484ae00d19"
            ),
            Tag(
                name = "Skirts",
                tips = listOf(
                    "Look for skirts made from eco-friendly materials like Tencel, organic cotton, or hemp.",
                    "Shop from thrift stores or clothing swaps to find sustainable and unique skirt options.",
                    "Support brands that prioritize transparency and ethical sourcing in their supply chain.",
                    "Consider versatile skirt styles that can be worn in multiple seasons and occasions.",
                    "Explore DIY or upcycling projects to transform old skirts into new fashion pieces."
                ),
                image = "https://firebasestorage.googleapis.com/v0/b/wowdrobe-e3d22.appspot.com/o/skirts.png?alt=media&token=355549f4-c069-430b-9c3f-10bf22d624fa"
            ),
            Tag(
                name = "Trousers",
                tips = listOf(
                    "Opt for trousers made from sustainable fabrics like Tencel, bamboo, or recycled polyester.",
                    "Shop pre-owned or vintage trousers to reduce the environmental impact of your wardrobe.",
                    "Support brands that offer size-inclusive options and prioritize fair labor practices.",
                    "Consider the versatility and longevity of trousers when making purchasing decisions.",
                    "Explore capsule wardrobe concepts to curate a timeless and sustainable trouser collection."
                ),
                image = "https://firebasestorage.googleapis.com/v0/b/wowdrobe-e3d22.appspot.com/o/trousers.png?alt=media&token=302fa31e-624e-4992-96a8-aae548334195"
            ),
        )
    ),
    Groups(
        name = "Outerwear",
        tags = listOf(
            Tag(
                name = "Jackets",
                tips = listOf(
                    "Look for jackets made from sustainable materials like recycled nylon or organic wool.",
                    "Shop vintage or second-hand jackets to reduce fashion waste and support circular fashion.",
                    "Support brands that offer repair services or take-back programs for worn-out jackets.",
                    "Consider the environmental impact of garment production and transportation when purchasing jackets.",
                    "Explore layering and versatile jacket styles to maximize their utility and minimize wardrobe turnover."
                ),
                image = "https://firebasestorage.googleapis.com/v0/b/wowdrobe-e3d22.appspot.com/o/jackets.png?alt=media&token=14e5d01c-e934-4186-aaec-b812e2e5eeb0"
            ),
            Tag(
                name = "Coats",
                tips = listOf(
                    "Choose coats made from eco-friendly materials like recycled polyester or cruelty-free down alternatives.",
                    "Shop for second-hand or vintage coats to reduce textile waste and support sustainable fashion.",
                    "Consider the durability and timeless design of coats to ensure long-term wearability.",
                    "Support brands that prioritize transparency and ethical practices in their supply chain.",
                    "Explore garment care and maintenance practices to prolong the lifespan of your coats."
                ),
                image = "https://firebasestorage.googleapis.com/v0/b/wowdrobe-e3d22.appspot.com/o/coats.png?alt=media&token=1088476c-9692-4e22-911d-3370e24464df"
            ),
            Tag(
                name = "Sweaters",
                tips = listOf(
                    "Opt for sweaters made from sustainable fibers like organic cotton, bamboo, or recycled wool.",
                    "Shop from thrift stores or clothing rental platforms to find eco-friendly and affordable sweater options.",
                    "Support brands that prioritize fair labor practices and minimize environmental impact in their production.",
                    "Consider timeless sweater styles and neutral colors to ensure versatility and longevity in your wardrobe.",
                    "Explore DIY repair or upcycling projects to refresh old sweaters and extend their lifespan."
                ),
                image = "https://firebasestorage.googleapis.com/v0/b/wowdrobe-e3d22.appspot.com/o/sweaters.png?alt=media&token=284bced5-9b58-4afe-889a-17c07cfeb8c4"
            )
        )
    ),
    Groups(
        name = "Accessories",
        tags = listOf(
            Tag(
                name = "Scarves",
                tips = listOf(
                    "Look for scarves made from sustainable materials like organic cotton, bamboo, or recycled fibers.",
                    "Shop from ethical and transparent brands that prioritize fair labor practices and environmental responsibility.",
                    "Consider the versatility and timeless design of scarves to ensure long-term wearability.",
                    "Explore DIY or upcycling projects to repurpose old scarves into new fashion accessories or home decor.",
                    "Support artisans or small businesses that offer handmade and unique scarf options."
                ),
                image = "https://firebasestorage.googleapis.com/v0/b/wowdrobe-e3d22.appspot.com/o/scarf.png?alt=media&token=48a5f5f4-68c3-4322-8a5a-1944254aeb81"
            ),
            Tag(
                name = "Hats",
                tips = listOf(
                    "Choose hats made from sustainable materials like organic cotton, hemp, or recycled fibers.",
                    "Shop for second-hand or vintage hats to reduce fashion waste and support circular fashion.",
                    "Consider the durability and sun protection features of hats when making purchasing decisions.",
                    "Support brands that prioritize ethical production practices and offer transparency in their supply chain.",
                    "Explore DIY customization or embellishment projects to personalize old hats and give them new life."
                ),
                image = "https://firebasestorage.googleapis.com/v0/b/wowdrobe-e3d22.appspot.com/o/hats.png?alt=media&token=4155e03a-605b-460b-8363-15b81c1de080"
            ),
            Tag(
                name = "Bags",
                tips = listOf(
                    "Opt for bags made from sustainable materials like organic cotton, canvas, or recycled plastics.",
                    "Shop from brands that prioritize ethical labor practices and environmental responsibility in their production.",
                    "Consider the functionality and versatility of bags to ensure they meet your lifestyle needs.",
                    "Support local artisans or small businesses that offer handmade and unique bag options.",
                    "Explore DIY repair or upcycling projects to extend the lifespan of old bags and reduce fashion waste."
                ),
                image = "https://firebasestorage.googleapis.com/v0/b/wowdrobe-e3d22.appspot.com/o/bags.png?alt=media&token=946851fa-77f0-4782-b069-b686b0e8106b"
            ),

            // Add more tags and tips for Metal Waste...
        )
    ),

)

val allTags = getAllTagsFromGroups(wasteGroups)

fun getAllTagsFromGroups(wasteGroups: List<Groups>): List<Tag> {
    return wasteGroups.flatMap { it.tags }
}
