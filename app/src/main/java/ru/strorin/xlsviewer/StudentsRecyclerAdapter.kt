package ru.strorin.xlsviewer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.strorin.xlsviewer.data.StudentDTO



class StudentsRecyclerAdapter() :
    RecyclerView.Adapter<StudentsRecyclerAdapter.MyViewHolder>() {

    private var students: MutableList<StudentDTO> = ArrayList<StudentDTO>()

    override fun getItemCount(): Int {
        return students.size
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, p1: Int) {
        viewHolder.bind(students[p1]);
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.students_item, parent, false)
        return MyViewHolder(view)
    }

    fun setDataset(studentsArray: MutableList<StudentDTO>) {
        students.clear()
        students.addAll(studentsArray)
        notifyDataSetChanged()
    }

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val mOrdinalNumView: TextView = v.findViewById(R.id.ord_num)
        private val mDatabaseNumView: TextView = v.findViewById(R.id.db_num)
        private val mNameView: TextView = v.findViewById(R.id.name_view)
        private val mScoresView: TextView = v.findViewById(R.id.scores_num)
        private val mPlaceView: TextView = v.findViewById(R.id.place_view)

        fun bind(studentItem: StudentDTO) {
            mOrdinalNumView.text = studentItem.num.toString()
            mDatabaseNumView.text = studentItem.regNum.toString()
            mNameView.text = studentItem.name
            mScoresView.text = studentItem.scores.toString()
            mPlaceView.text = studentItem.place
        }
    }
}