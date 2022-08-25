package com.kjy.apiproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kjy.apiproject.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 커스텀 어댑터 클래스를 변수로 가져오고 리사이클러뷰 어댑터와 연결
        // 리사이클러뷰 형식을 세로 기본으로 지정.
        val adapter = CustomAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.buttonRequest.setOnClickListener {
            // githubService에느 GithubService 인터페이스를 통해 호출 했기 때문에 users() 메서드가 포함되어 있음
            // create() 메서드를 통해 실행 가능한 서비스 객체로 만들고 users() 메서드 안에 비동기 통신으로 데이터를 가져오는
            // enqueue 메서드를 추가. 이 메소드가 호출되면 통신 시작
            // 호출 시 Github API 서버로부터 응답을 받으면 enqueue() 안에 작성하는 콜백 인터페이스가 작동.
            val githubService = retrofitClass.githubService
            githubService.users().enqueue(object: Callback<Repository> {
                // 콜백 인터페이스의 필수 메서드들
                // 응답을 정상적으로 받았을 때
                override fun onResponse(call: Call<Repository>, response: Response<Repository>) {
                    // reponse의 body() 메서드 호출시 서버로부터 전송된 데이터를 꺼낼 수 있음.
                    // 꺼낸 데이터를 List<Repository>로 형변환 한 후에 어댑터의 userList에 담음.
                    adapter.userList = response.body() as Repository
                    adapter.notifyDataSetChanged()

                }

                // 응답에 실패 했을 때
                override fun onFailure(call: Call<Repository>, t: Throwable) {
                }

            })
        }
    }
}

// 레트로핏 인터페이스는 호출 방식, 주소 데이터 등을 지정한다.
// Retrofit 라이브러리는 인터페이스를 해석해 Http 통신을 처리
interface GithubService {
    // GithubApi를 호출하기 위해서 @GET 애노테이션을 사용해서 요청 주소 설정
    // 도메인은 제외하고 작성. 반환값은 Call<List<데이터 클래스>> 형태로 작성한다.
    // retrofit2 패키지에 있는 것을 선택
    // 레트로핏은 이렇게 만들어진 인터페이스에 지정된 방식으로 서버와 통신하고 데이터를 가져옴.
    @GET("users/Kotlin/repos")
    fun users(): Call<Repository>

}

// Retrofit을 싱글톤 패턴으로 구현
object retrofitClass {
    // Retrofit.Builder()를 사용해서 레트로핏을 생성하고 retrofit 변수에 담는다.
    // baseUrl이되는 Github의 도메인 주소와 JSON 데이터를 앞에서 생성한 Repository 클래스의 컬렉션으로 변환
    // 해주는 컨버터를 입력하고 build() 메서드를 호출해서 생성.
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val githubService = retrofit.create(GithubService::class.java)
}

