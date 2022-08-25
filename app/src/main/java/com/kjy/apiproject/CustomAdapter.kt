package com.kjy.apiproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kjy.apiproject.databinding.ItemRecyclerBinding

// 리스트를 담기위한 리사이클러뷰
class CustomAdapter: RecyclerView.Adapter<Holder>() {

    // 어댑터에서 사용할 데이터 컬렉션을 변수로 설정
    var userList: Repository? = null

    // 홀더를 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)

    }
    // 실제 목록에 뿌려지는 아이템을 그려줌.
    // 현 위치의 사용자 데이터를 userList에서 가져오고 아직 만들어지지 않은 홀더의 setUser() 메서드에 넘겨줌
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = userList?.get(position)
        holder.setUser(user)
    }


    override fun getItemCount(): Int {
        // elvis 연산자 사용
        // 객체의 값이 null이 아니라면 userList.size를 반환하고 null이라면 0을 반환한다.
       return userList?.size?: 0
    }
}

class Holder(val binding: ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root) {
    // setUser 메서드는 1개의 ReposioryItem을 파라미터로 사용.
    // 클래스의 맨 윗줄 userList를 가져올때 Repository를 nullable로 설정했기 때문에 이때도 nullable로 가져옴.
    // 아바타주소 : user.owner.avatar_url
    // 사용자 이름 : user.name
    // 사용자 아이디 :user.node_id
    fun setUser(user: RepositoryItem?) {
        user?.let {
            binding.textName.text = user?.name
            binding.textId.text = user?.node_id
            Glide.with(binding.imageAvatar)
                .load(user.owner.avatar_url)
                .into(binding.imageAvatar)
        }

    }

}