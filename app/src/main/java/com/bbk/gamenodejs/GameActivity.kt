package com.bbk.gamenodejs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bbk.gamenodejs.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var socketHandler: SocketHandler
    private lateinit var binding: ActivityGameBinding

    private var userName = ""

    private var myScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra(USERNAME) ?: ""

        if (userName.isEmpty()) {
            finish()
        } else {
            binding.selfPlayerTextView.text = userName
            binding.selfPlayerScoreTextView.text = myScore.toString()

            socketHandler = SocketHandler()

            binding.scoreButton.setOnClickListener {
                myScore++

                val score = Score(
                    username = userName,
                    score = myScore
                )
                socketHandler.emitScore(score)
            }

            binding.score2Button.setOnClickListener {
                myScore += 2

                val score = Score(
                    username = userName,
                    score = myScore
                )
                socketHandler.emitScore2(score)
            }

            socketHandler.onNewScore.observe(this) { incomingData ->

                if (incomingData.username == userName) {
                    binding.selfPlayerScoreTextView.text = incomingData.score.toString()
                } else {
                    binding.otherPlayerTextView.text = incomingData.username
                    binding.otherPlayerScoreTextView.text = incomingData.score.toString()
                }

                binding.selfPlayerTextView.setTextColor(resources.getColor(R.color.black))
                binding.selfPlayerScoreTextView.setTextColor(resources.getColor(R.color.black))
                binding.otherPlayerTextView.setTextColor(resources.getColor(R.color.black))
                binding.otherPlayerScoreTextView.setTextColor(resources.getColor(R.color.black))
            }

            socketHandler.onNewScore2.observe(this) { incomingData ->

                if (incomingData.username == userName) {
                    binding.selfPlayerScoreTextView.text = incomingData.score.toString()
                } else {
                    binding.otherPlayerTextView.text = incomingData.username
                    binding.otherPlayerScoreTextView.text = incomingData.score.toString()
                }

                binding.selfPlayerTextView.setTextColor(resources.getColor(R.color.purple_700))
                binding.selfPlayerScoreTextView.setTextColor(resources.getColor(R.color.purple_700))
                binding.otherPlayerTextView.setTextColor(resources.getColor(R.color.purple_700))
                binding.otherPlayerScoreTextView.setTextColor(resources.getColor(R.color.purple_700))
            }
        }
    }

    override fun onDestroy() {
        socketHandler.disconnectSocket()
        super.onDestroy()
    }

    companion object{
        const val USERNAME = "username"
    }
}