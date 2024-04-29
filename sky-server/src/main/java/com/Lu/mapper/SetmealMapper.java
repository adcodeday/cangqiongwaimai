package com.Lu.mapper;

import com.Lu.annocation.AutoFill;
import com.Lu.dto.SetmealPageQueryDTO;
import com.Lu.entity.Setmeal;
import com.Lu.enumeration.OperationType;
import com.Lu.vo.SetmealVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);
    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long setmealId);
    //TODO uodate没写
    @AutoFill(OperationType.UPDATE)
    @Update("update setmeal set status=#{status} where id=#{id}")
    void update(Setmeal setmeal);
}
