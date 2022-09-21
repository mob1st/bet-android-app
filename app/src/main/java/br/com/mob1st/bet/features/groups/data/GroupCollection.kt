package br.com.mob1st.bet.features.groups.data

import br.com.mob1st.bet.core.firebase.awaitWithTimeout
import br.com.mob1st.bet.features.competitions.data.competitions
import br.com.mob1st.bet.features.groups.domain.Group
import br.com.mob1st.bet.features.profile.data.memberships
import br.com.mob1st.bet.features.profile.data.users
import br.com.mob1st.bet.features.profile.domain.User
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.annotation.Factory

@Factory
class GroupCollection(
    private val firestore: FirebaseFirestore
) {
    suspend fun create(
        founder: User,
        group: Group,
    ) {
        /*
        Esses arquivos "Collection" servem pra isolar o Firebase do resto do projeto.
        Fazer isso é uma forma de seguir Clean Architeture, mantendo o código de regras de negócio
        isolado do código que interage com framework.

        A vantagem é que podemos no futuro remover o firebase sem precisar alterar as outras camadas
        do app.
        */

        val batch = firestore.batch()
        val groupRef = firestore.groups.document()
        batch.set(
            groupRef,
            mapOf(
                "name" to group.name,
                "imageUrl" to group.imageUrl.orEmpty(),
                "membersCount" to group.memberCount,
                "description" to group.description,
                "createdAt" to group.createdAt,
                "updatedAt" to group.updatedAt,
                "competition" to mapOf(
                    "name" to group.competition.name,
                    "type" to group.competition.type.name,
                    "ref" to firestore.competitions.document(group.competition.id)
                )
            )
        )

        val memberRef = firestore.members(groupRef.id).document()
        batch.set(
            memberRef,
            mapOf(
                "ref" to firestore.users.document(founder.id),
                "name" to founder.name,
                "image" to founder.imageUrl.orEmpty(),

                // depois temos que ver como vamos armazenar os pontos
                "points" to 0L
            )
        )

        val membershipRef = firestore.memberships(founder.id)
            .document()
        batch.set(
            membershipRef,
            mapOf(
                "name" to group.name,
                "url" to group.imageUrl.orEmpty(),
                "ref" to groupRef
            )
        )
        batch.commit().awaitWithTimeout()
    }

}

val FirebaseFirestore.groups get() =
    collection("groups")
fun FirebaseFirestore.members(groupId: String) =
    groups.document(groupId).collection("members")
